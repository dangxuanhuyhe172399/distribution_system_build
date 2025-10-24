package com.sep490.bads.distributionsystem.service.impl;

import com.sep490.bads.distributionsystem.dto.LoginDto;
import com.sep490.bads.distributionsystem.dto.response.AuthResponse;
import com.sep490.bads.distributionsystem.entity.User;
import com.sep490.bads.distributionsystem.exception.NotAuthorizedException;
import com.sep490.bads.distributionsystem.repository.UserRepository;
import com.sep490.bads.distributionsystem.security.jwt.JwtService;
import com.sep490.bads.distributionsystem.security.jwt.TokenInfo;
import com.sep490.bads.distributionsystem.service.AuthService;
import com.sep490.bads.distributionsystem.utils.Constants;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwt;

    public AuthServiceImpl(UserRepository userRepo, PasswordEncoder passwordEncoder, JwtService jwt) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwt = jwt;
    }

    @Override
    public AuthResponse signIn(LoginDto dto) {
        User u = userRepo.findActiveByUsername(dto.getUsername())
                .orElseThrow(() -> new NotAuthorizedException(Constants.INCORRECT_USERNAME_OR_PASSWORD));

        if (u.getStatus() == null || !u.getStatus())
            throw new NotAuthorizedException(Constants.INACTIVE_USER);

        if (!passwordEncoder.matches(dto.getPassword(), u.getPassword()))
            throw new NotAuthorizedException(Constants.INCORRECT_USERNAME_OR_PASSWORD);

        String token = jwt.generate(u);
        return new AuthResponse(token, jwt.expirySeconds());
    }

    @Override
    public void forgotPassword(String email) {

    }
}
