package com.bhushan.jwt.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bhushan.jwt.app.entity.EnumRole;
import com.bhushan.jwt.app.entity.Role;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    Optional<Role> findByName(EnumRole name);
}
