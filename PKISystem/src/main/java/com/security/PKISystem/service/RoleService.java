package com.security.PKISystem.service;

import com.security.PKISystem.domain.Role;

import java.util.List;

public interface RoleService {

    List<Role> findById(Long id);

    List<Role> findByName(String name);
}
