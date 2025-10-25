package com.sep490.bads.distributionsystem.service.impl;

import com.sep490.bads.distributionsystem.dto.LoginDto;
import com.sep490.bads.distributionsystem.dto.response.AuthResponse;
import com.sep490.bads.distributionsystem.entity.User;
import com.sep490.bads.distributionsystem.exception.NotAuthorizedException;
import com.sep490.bads.distributionsystem.exception.WrongAccountOrPasswordException;
import com.sep490.bads.distributionsystem.mapper.AuthMapper;
import com.sep490.bads.distributionsystem.repository.UserRepository;
import com.sep490.bads.distributionsystem.service.AuthService;
import com.sep490.bads.distributionsystem.utils.Constants;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthMapper authMapper;

    public AuthServiceImpl(UserRepository userRepo, PasswordEncoder passwordEncoder, AuthMapper authMapper) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
     //   this.jwt = jwt;
        this.authMapper = authMapper;
    }

    @Override
    public AuthResponse signIn(LoginDto dto) {
        User u = userRepo.findActiveByUsername(dto.getUsername())
                .orElseThrow(() -> new NotAuthorizedException(Constants.INCORRECT_USERNAME_OR_PASSWORD));

        if (u == null) {
            throw new WrongAccountOrPasswordException();
        }
        throw new NotAuthorizedException(Constants.INACTIVE_USER);
    }

    @Override
    public void forgotPassword(String email) {

    }

    @Override
    public void resetPasswordByOtp(String email, String otp, String newPassword) {

    }
}
