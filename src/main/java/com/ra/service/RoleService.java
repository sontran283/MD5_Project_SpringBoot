package com.ra.service;


import com.ra.model.entity.Role;

public interface RoleService {
    Role findByRoleName(String name);
}