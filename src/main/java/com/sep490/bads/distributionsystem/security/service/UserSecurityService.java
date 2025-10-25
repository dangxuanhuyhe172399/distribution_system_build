package com.sep490.bads.distributionsystem.security.service;

import com.sep490.bads.distributionsystem.entity.Role;
import com.sep490.bads.distributionsystem.entity.User;
import com.sep490.bads.distributionsystem.service.impl.RoleServiceImpl;
import com.sep490.bads.distributionsystem.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserSecurityService implements UserDetailsService {

    @Autowired
    protected UserServiceImpl userServiceImpl;
    @Autowired
    protected RoleServiceImpl roleServiceImpl;



    @Override
    public UserDetails loadUserByUsername(String subject) throws UsernameNotFoundException {
        final Long userId;
        try {
            userId = Long.parseLong(subject); // subject = userId từ JWT
        } catch (NumberFormatException e) {
            throw new UsernameNotFoundException("Invalid token subject: " + subject);
        }

        User u = userServiceImpl.findById(userId);
        if (u == null) throw new UsernameNotFoundException("User not found " + subject);

        // Lấy role
        String roleName = (u.getRole() != null)
                ? u.getRole().getRoleName()
                : roleServiceImpl.findRoleByUserId(userId).map(Role::getRoleName).orElse("USER");

        List<GrantedAuthority> auths = List.of(new SimpleGrantedAuthority("ROLE_" + roleName));

        return new org.springframework.security.core.userdetails.User(
                u.getUsername(),
                u.getPassword(),
                Boolean.TRUE.equals(u.getStatus()),  // enabled
                true, true, true,
                auths
        );
    }
}
