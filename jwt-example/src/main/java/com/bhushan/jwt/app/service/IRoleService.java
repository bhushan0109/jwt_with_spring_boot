package com.bhushan.jwt.app.service;

import java.util.Optional;

import com.bhushan.jwt.app.entity.EnumRole;
import com.bhushan.jwt.app.entity.Role;

public interface IRoleService {
    Optional<Role> findByName(EnumRole name);
}
