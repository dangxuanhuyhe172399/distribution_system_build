package com.sep490.bads.distributionsystem.service;

import com.sep490.bads.distributionsystem.dto.LoginDto;
import com.sep490.bads.distributionsystem.dto.response.AuthResponse;
import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    AuthResponse signIn(LoginDto dto);

    void forgotPassword(String email);

    void resetPasswordByOtp( String email,  String otp, String newPassword);
}

