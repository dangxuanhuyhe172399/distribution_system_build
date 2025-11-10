package com.sep490.bads.distributionsystem.config.security.service;

import com.sep490.bads.distributionsystem.entity.Role;
import com.sep490.bads.distributionsystem.entity.User;
import com.sep490.bads.distributionsystem.entity.type.UserStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Setter
@Getter
public class UserDetailsImpl implements UserDetails {

    private final Long userId;
    private final String username;
    private final String password;
    private final Set<GrantedAuthority> authorities;
    private final boolean enabled;

    public UserDetailsImpl(User user, Set<Role> roles) {
        this.userId = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.enabled = user.getStatus() == null || user.getStatus() == UserStatus.ACTIVE;

        this.authorities = (roles == null ? Set.<Role>of() : roles).stream()
                .map(r -> new SimpleGrantedAuthority("ROLE_" + r.getRoleName().toUpperCase()))
                .collect(Collectors.toSet());
    }

    @Override public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return enabled; }
}
