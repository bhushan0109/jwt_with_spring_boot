package com.bhushan.jwt.app.service;

import java.util.Optional;

import com.bhushan.jwt.app.entity.RefreshToken;
import com.bhushan.jwt.app.entity.User;

public interface IUserService {

    void saveUser(User user);
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    User findById(int userId);
}
