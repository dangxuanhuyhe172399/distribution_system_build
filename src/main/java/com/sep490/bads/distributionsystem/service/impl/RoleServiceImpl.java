package com.sep490.bads.distributionsystem.service.impl;

import com.sep490.bads.distributionsystem.entity.Role;
import com.sep490.bads.distributionsystem.repository.RoleRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@Log4j2
public class RoleServiceImpl {

    @Autowired
    protected RoleRepository roleRepository;

    public Optional<Role> findRoleByUserId(Long id) {
        return roleRepository.findRoleByUserId(id);
    }
}
