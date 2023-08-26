package com.bhushan.jwt.app.service;

import java.util.Optional;

import com.bhushan.jwt.app.entity.RefreshToken;

public interface IRefreshTokenService {

    public Optional<RefreshToken> findByToken(String token);
    public RefreshToken verifyExpiration(RefreshToken token);
    public int deleteByUserId(int userId);
    public RefreshToken createRefreshToken(int userId, String ip);
}
