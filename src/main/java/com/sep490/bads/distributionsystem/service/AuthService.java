package com.sep490.bads.distributionsystem.service;


import com.sep490.bads.distributionsystem.dto.LoginDto;
import com.sep490.bads.distributionsystem.dto.response.AuthResponse;
import org.springframework.stereotype.Service;

@Service

public interface AuthService {
    AuthResponse signIn(LoginDto dto);
    void forgotPassword(String email);
}

