// src/main/java/com/sep490/bads/distributionsystem/config/security/jwt/UserPrincipal.java

package com.sep490.bads.distributionsystem.config.security.jwt;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class UserPrincipal implements UserDetails {
    private Long id;                    // <-- ĐÃ CÓ FIELD NÀY CHƯA?
    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    // Constructor
    public UserPrincipal(Long id, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    // THÊM METHOD NÀY VÀO ĐÂY:
    public Long getId() {
        return id;
    }
    // Nếu bạn muốn dùng ở nơi khác thì thêm luôn:
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}