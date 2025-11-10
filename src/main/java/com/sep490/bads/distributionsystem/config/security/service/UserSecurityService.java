package com.sep490.bads.distributionsystem.config.security.service;

import com.sep490.bads.distributionsystem.entity.Role;
import com.sep490.bads.distributionsystem.entity.User;
import com.sep490.bads.distributionsystem.service.impl.RoleServiceImpl;
import com.sep490.bads.distributionsystem.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserSecurityService implements UserDetailsService {

    private final UserServiceImpl userServiceImpl;;
    private final RoleServiceImpl roleServiceImpl;

    @Override
    public UserDetails loadUserByUsername(String subject) throws UsernameNotFoundException {
        if (subject == null || subject.isBlank()) {
            throw new UsernameNotFoundException("Empty token subject");
        }

        User u;
        if (subject.chars().allMatch(Character::isDigit)) {
            u = userServiceImpl.findByIdWithRole(Long.parseLong(subject));
        } else {
            u = userServiceImpl.findByUsernameWithRole(subject);
        }
        if (u == null) throw new UsernameNotFoundException("User not found: " + subject);

        var roles = new java.util.HashSet<Role>();
        if (u.getRole() != null) roles.add(u.getRole());
        roleServiceImpl.findRoleByUserId(u.getId()).ifPresent(roles::add);

        return new UserDetailsImpl(u, roles);
    }
}
