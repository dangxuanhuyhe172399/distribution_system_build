package com.tal.recruitment.system.security;

import com.tal.recruitment.system.entity.User;
import lombok.*;
import org.springframework.security.core.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
public class CustomUserDetails implements UserDetails {

    private final Long userId;
    private final String email;
    private final String password;
    private final String fullName;
    private final String roleName;
    private final boolean enabled;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(Long userId, String email, String password,
                             String fullName, String roleName, boolean enabled) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.roleName = roleName;
        this.enabled = enabled;
        this.authorities = List.of(new SimpleGrantedAuthority("ROLE_" + roleName.toUpperCase()));
    }

    public static CustomUserDetails from(User user) {
        String role = user.getRole() != null ? user.getRole().getRoleName() : "USER";
        boolean isEnabled = !"INACTIVE".equalsIgnoreCase(String.valueOf(user.getUserStatus()));

        return new CustomUserDetails(
                user.getUserId(),
                user.getUserEmail(),
                user.getPassword(),
                user.getFullName(),
                role,
                isEnabled
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities; // Không còn truy cập entity
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public String getRole() {
        return roleName;
    }

    public String getFullName() {
        return fullName;
    }
}
