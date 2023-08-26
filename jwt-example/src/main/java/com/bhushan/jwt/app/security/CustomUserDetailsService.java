package com.bhushan.jwt.app.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.bhushan.jwt.app.entity.User;
import com.bhushan.jwt.app.exception.UserNotFoundException;
import com.bhushan.jwt.app.service.impl.UserService;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userService.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User Name " + username + " not found"));

        return new CustomUserDetails(user);
    }
}
