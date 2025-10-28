package com.sep490.bads.distributionsystem.service.impl;

import com.sep490.bads.distributionsystem.dto.LoginDto;
import com.sep490.bads.distributionsystem.dto.response.AuthResponse;
import com.sep490.bads.distributionsystem.entity.User;
import com.sep490.bads.distributionsystem.entity.type.UserStatus;
import com.sep490.bads.distributionsystem.exception.NotAuthorizedException;
import com.sep490.bads.distributionsystem.exception.WrongAccountOrPasswordException;
import com.sep490.bads.distributionsystem.mapper.AuthMapper;
import com.sep490.bads.distributionsystem.repository.UserRepository;
import com.sep490.bads.distributionsystem.security.jwt.JwtService;
import com.sep490.bads.distributionsystem.security.jwt.TokenInfo;
import com.sep490.bads.distributionsystem.service.AuthService;
import com.sep490.bads.distributionsystem.utils.Constants;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Log4j2
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthMapper authMapper;

    public AuthServiceImpl(UserRepository userRepo, PasswordEncoder passwordEncoder, JwtService jwtService, AuthMapper authMapper) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authMapper = authMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponse signIn(LoginDto dto) {
        User u = userRepo.findByUsernameWithRole(dto.getUsername())
                .orElseThrow(WrongAccountOrPasswordException::new);

        if (!passwordEncoder.matches(dto.getPassword(), u.getPassword())) {
            throw new WrongAccountOrPasswordException();
        }
        if (u.getStatus() != UserStatus.ACTIVE) {
            throw new NotAuthorizedException(Constants.INACTIVE_USER);
        }

        TokenInfo info = TokenInfo.createFrom(u);
        String token = jwtService.generateToken(info);

        return AuthResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .refreshToken(null) // chưa dùng
                .userId(u.getId())
                .userEmail(u.getEmail())
                .userName(u.getUsername())
                .fullName(u.getFullName())
                .role(u.getRole() != null ? u.getRole().getRoleName() : null)
                .expiresAt(Instant.now().plusSeconds(jwtService.getExpireTime()))
                .build();
    }

    @Override
    public void forgotPassword(String email) {

    }

    @Override
    public void resetPasswordByOtp(String email, String otp, String newPassword) {

    }
}
