package com.bhushan.jwt.app.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bhushan.jwt.app.entity.RefreshToken;
import com.bhushan.jwt.app.entity.Role;
import com.bhushan.jwt.app.entity.User;
import com.bhushan.jwt.app.exception.RefreshTokenException;
import com.bhushan.jwt.app.repository.RefreshTokenRepository;
import com.bhushan.jwt.app.service.IRefreshTokenService;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class RefreshTokenService implements IRefreshTokenService {

    @Value("${jwt.secret.refrEshexpireMs}")
    private Long refreshTokenDurationMs;

    RefreshTokenRepository refreshTokenRepository;

    UserService userService;

    @Autowired
    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository,UserService userService) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userService = userService;
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Override
    public RefreshToken createRefreshToken(int userId, String ip) {

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(userService.findById(userId));
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setIpAddress(ip);

        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {

        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
        }

        return token;
    }

    @Override
    public int deleteByUserId(int userId) {

        User user = userService.findById(userId);
        return refreshTokenRepository.deleteByUser(user);
    }

	public Optional<RefreshToken> findByTokenAndIpAddress(String requestRefreshToken, String ip) {
		return refreshTokenRepository.findByTokenAndIpAddress(requestRefreshToken,ip);
	}
}
