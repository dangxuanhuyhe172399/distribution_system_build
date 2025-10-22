package com.tal.recruitment.system.service;

import com.tal.recruitment.system.dto.request.AuthRequest;
import com.tal.recruitment.system.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse login(AuthRequest request);
}
