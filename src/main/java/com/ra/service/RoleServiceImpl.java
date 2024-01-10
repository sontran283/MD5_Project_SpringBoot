package com.ra.service;

import com.ra.model.entity.Role;
import com.ra.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService{
    @Autowired
    private RoleRepository roleRepository;
    @Override
    public Role findByRoleName(String name) {
        return roleRepository.findRoleByName(name);
    }
}