package com.bhushan.jwt.app.controller;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bhushan.jwt.app.entity.EnumRole;
import com.bhushan.jwt.app.entity.RefreshToken;
import com.bhushan.jwt.app.entity.Role;
import com.bhushan.jwt.app.entity.User;
import com.bhushan.jwt.app.exception.RefreshTokenException;
import com.bhushan.jwt.app.exception.RoleException;
import com.bhushan.jwt.app.request.LoginRequest;
import com.bhushan.jwt.app.request.SignupRequest;
import com.bhushan.jwt.app.request.TokenRefreshRequest;
import com.bhushan.jwt.app.response.JWTResponse;
import com.bhushan.jwt.app.response.MessageResponse;
import com.bhushan.jwt.app.response.TokenRefreshResponse;
import com.bhushan.jwt.app.security.CustomUserDetails;
import com.bhushan.jwt.app.security.JwtUtils;
import com.bhushan.jwt.app.service.impl.RefreshTokenService;
import com.bhushan.jwt.app.service.impl.RoleService;
import com.bhushan.jwt.app.service.impl.UserService;
import com.bhushan.jwt.app.utils.CommonUtil;

import springfox.documentation.annotations.ApiIgnore;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

	@Autowired
	UserService userService;

	@Autowired
	RoleService roleService;

	@Autowired
	RefreshTokenService refreshTokenService;

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;



	@PostMapping("/registerUser")
	public ResponseEntity<?> registerUser( @Valid  @RequestBody SignupRequest signUpRequest,
			HttpServletRequest httpServletRequest) {

		LOGGER.info("AuthController | registerUser is started");

		String username = signUpRequest.getUsername();
		String email = signUpRequest.getEmail();
		String password = signUpRequest.getPassword();
		Set<String> strRoles = signUpRequest.getRoles();
		Set<Role> roles = new HashSet<>();

		if (userService.existsByUsername(username)) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
		}

		if (userService.existsByEmail(email)) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already taken!"));
		}

		User user = new User();
		user.setEmail(email);
		user.setUsername(username);
		user.setPassword(encoder.encode(password));

		if (strRoles != null) {
			strRoles.forEach(role -> {
				LOGGER.info("AuthController | registerUser | role : " + role);
				switch (role) {
				case "ROLE_ADMIN":

					Role adminRole = null;

					if (roleService.findByName(EnumRole.ROLE_ADMIN).isPresent()) {
						adminRole = roleService.findByName(EnumRole.ROLE_ADMIN).get();
						// adminRole = new Role(ERole.ROLE_ADMIN);
					} else {
						adminRole = roleService.findByName(EnumRole.ROLE_ADMIN)
								.orElseThrow(() -> new RoleException("Error: Admin Role is not found."));
					}

					roles.add(adminRole);
					break;

				case "ROLE_HR":

					Role moderatorRole = null;

					if (roleService.findByName(EnumRole.ROLE_HR).isPresent()) {
						moderatorRole = roleService.findByName(EnumRole.ROLE_HR).get();
						// moderatorRole = new Role(ERole.ROLE_HR);
					} else {
						moderatorRole = roleService.findByName(EnumRole.ROLE_HR)
								.orElseThrow(() -> new RoleException("Error: Moderator Role is not found."));
					}

					roles.add(moderatorRole);

					break;

				case "ROLE_USER":

					Role userRole = null;

					if (roleService.findByName(EnumRole.ROLE_USER).isPresent()) {
						userRole = roleService.findByName(EnumRole.ROLE_USER).get();
						// userRole = new Role(ERole.ROLE_USER);
					} else {
						userRole = roleService.findByName(EnumRole.ROLE_USER)
								.orElseThrow(() -> new RoleException("Error: User Role is not found."));
					}

					roles.add(userRole);

				}

			});
		} else {
			new RoleException("Error:  Role is not found.");
		}

		roleService.saveRoles(roles);
		user.setRoles(roles);
		userService.saveUser(user);

		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}

	@PostMapping("/authenticateUser")
	public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest,
			HttpServletRequest httpServletRequest) {

		LOGGER.info("AuthController | authenticateUser is started");

		String username = loginRequest.getUsername();
		String password = loginRequest.getPassword();

		LOGGER.info("AuthController | authenticateUser | username : " + username);
		LOGGER.info("AuthController | authenticateUser | password : " + password);

		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
				username, password);

		LOGGER.info("AuthController | authenticateUser | usernamePasswordAuthenticationToken : "
				+ usernamePasswordAuthenticationToken.toString());

		Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		String jwt = jwtUtils.generateJwtToken(userDetails);

		LOGGER.info("AuthController | authenticateUser | jwt : " + jwt);

		List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
				.collect(Collectors.toList());

		LOGGER.info("AuthController | authenticateUser | roles : " + roles.toString());
		String ip = CommonUtil.getClientIp(httpServletRequest);
		LOGGER.info("AuthController | refreshtoken | ip: " + ip);
		RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId(), ip);

		LOGGER.info("AuthController | authenticateUser | refreshToken : " + refreshToken.toString());

		JWTResponse jwtResponse = new JWTResponse();
		jwtResponse.setEmail(userDetails.getEmail());
		jwtResponse.setUsername(userDetails.getUsername());
		jwtResponse.setId(userDetails.getId());
		jwtResponse.setToken(jwt);
		jwtResponse.setRefreshToken(refreshToken.getToken());
		jwtResponse.setRoles(roles);

		LOGGER.info("AuthController | authenticateUser | jwtResponse : " + jwtResponse.toString());

		return ResponseEntity.ok(jwtResponse);
	}

	@PostMapping("/logout")
	public ResponseEntity<?> logoutUser(@ApiIgnore Principal principalUser, HttpServletRequest httpServletRequest) {

		LOGGER.info("AuthController | logoutUser is started");

		Object principal = ((UsernamePasswordAuthenticationToken) principalUser).getPrincipal();
		CustomUserDetails user = (CustomUserDetails) principal;

		int userId = user.getId();

		LOGGER.info("AuthController | logoutUser | userId : " + userId);

		int deletedValue = refreshTokenService.deleteByUserId(userId);

		if (deletedValue == 1) {
			return ResponseEntity.ok(new MessageResponse("Log out successful!"));
		} else {
			return ResponseEntity.ok(new MessageResponse("You're already logout"));
		}

	}

	@PostMapping("/refreshtoken")
	public ResponseEntity<?> refreshtoken(@RequestBody TokenRefreshRequest request,
			HttpServletRequest httpServletRequest) {

		LOGGER.info("AuthController | refreshtoken is started");

		String requestRefreshToken = request.getRefreshToken();

		LOGGER.info("AuthController | refreshtoken | requestRefreshToken : " + requestRefreshToken);
		String ip = CommonUtil.getClientIp(httpServletRequest);

		LOGGER.info("AuthController | refreshtoken | ip: " + ip);
		RefreshToken token = refreshTokenService.findByTokenAndIpAddress(requestRefreshToken, ip).orElseThrow(
				() -> new RefreshTokenException(requestRefreshToken + "Refresh token is not in database!"));

		LOGGER.info("AuthController | refreshtoken | token : " + token.toString());

		RefreshToken deletedToken = refreshTokenService.verifyExpiration(token);

		LOGGER.info("AuthController | refreshtoken | deletedToken : " + deletedToken.toString());

		User userRefreshToken = deletedToken.getUser();

		LOGGER.info("AuthController | refreshtoken | userRefreshToken : " + userRefreshToken.toString());

		String newToken = jwtUtils.generateTokenFromUsername(userRefreshToken.getUsername());

		LOGGER.info("AuthController | refreshtoken | newToken : " + newToken);

		return ResponseEntity.ok(new TokenRefreshResponse(newToken, requestRefreshToken));

	}
}
