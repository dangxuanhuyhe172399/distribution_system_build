// java
package com.sep490.bads.distributionsystem.security.service;

import com.sep490.bads.distributionsystem.entity.Role;
import com.sep490.bads.distributionsystem.entity.User;
import com.sep490.bads.distributionsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;

@Service
public class UserSecurityService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserSecurityService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username == null || username.isBlank()) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        Set<Role> roles = user.getRole() == null ? Collections.emptySet() : Collections.singleton(user.getRole());
        return new UserDetailsImpl(user, roles);
    }
}
