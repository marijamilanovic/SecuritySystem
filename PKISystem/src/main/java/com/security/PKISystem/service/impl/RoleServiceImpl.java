package com.security.PKISystem.service.impl;

import com.security.PKISystem.domain.Role;
import com.security.PKISystem.repository.RoleRepository;
import com.security.PKISystem.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public List<Role> findById(Long id) {
        Role role = roleRepository.getOne(id);
        List<Role> roles = new ArrayList<>();
        roles.add(role);
        return roles;
    }

    @Override
    public List<Role> findByName(String name) {
        log.info("Try to find role: " + name);
        Role role = roleRepository.findByName(name);
        List<Role> roles = new ArrayList<>();
        roles.add(role);
        return roles;
    }
}
