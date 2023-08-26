package com.bhushan.jwt.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bhushan.jwt.app.entity.RefreshToken;
import com.bhushan.jwt.app.entity.Role;
import com.bhushan.jwt.app.entity.User;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {

    Optional<RefreshToken> findByToken(String token);

    @Modifying
    int deleteByUser(User user);

	Optional<RefreshToken> findByTokenAndIpAddress(String requestRefreshToken, String ip);
}
