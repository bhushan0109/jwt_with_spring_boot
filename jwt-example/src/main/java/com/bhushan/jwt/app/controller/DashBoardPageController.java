package com.bhushan.jwt.app.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dash")
public class DashBoardPageController {

	@GetMapping("/all")
	public String allAccess(HttpServletRequest httpServletRequest) {
		return "Public Content.";
	}

	@GetMapping("/user")
	@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN') or hasRole('ROLE_HR')")
	public String userAccess(HttpServletRequest httpServletRequest) {
		String response = " you are accessing user data.";
		return response;
	}

	@GetMapping("/admin")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String adminAccess(HttpServletRequest httpServletRequest) {
		String response = " you are accessing admin data.";
		return response;
	}

	@GetMapping("/hr")
	@PreAuthorize("hasRole('ROLE_HR')")
	public String hrAccess(HttpServletRequest httpServletRequest) {
		String response = " you are accessing HR data.";
		return response;
	}

}
