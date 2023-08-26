package com.bhushan.jwt.app.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bhushan.jwt.app.entity.EnumRole;
import com.bhushan.jwt.app.entity.Role;
import com.bhushan.jwt.app.repository.RoleRepository;
import com.bhushan.jwt.app.service.IRoleService;

import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class RoleService implements IRoleService {

    RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Optional<Role> findByName(EnumRole name) {
        return roleRepository.findByName(name);
    }

    public void saveRole(Role role){
        roleRepository.save(role);
    }

    public void saveRoles(Set<Role> roles){
        roleRepository.saveAll(roles);
    }
}
