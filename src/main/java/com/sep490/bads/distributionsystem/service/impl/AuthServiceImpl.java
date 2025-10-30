package com.sep490.bads.distributionsystem.service.impl;

import com.sep490.bads.distributionsystem.dto.LoginDto;
import com.sep490.bads.distributionsystem.dto.response.AuthResponse;
import com.sep490.bads.distributionsystem.entity.User;
import com.sep490.bads.distributionsystem.entity.type.UserStatus;
import com.sep490.bads.distributionsystem.exception.NotAuthorizedException;
import com.sep490.bads.distributionsystem.exception.WrongAccountOrPasswordException;
import com.sep490.bads.distributionsystem.repository.UserRepository;
import com.sep490.bads.distributionsystem.security.jwt.JwtService;
import com.sep490.bads.distributionsystem.security.jwt.TokenInfo;
import com.sep490.bads.distributionsystem.security.otp.InMemoryOtpStore;
import com.sep490.bads.distributionsystem.service.AuthService;
import com.sep490.bads.distributionsystem.service.MailService;
import com.sep490.bads.distributionsystem.utils.CacheKey;
import com.sep490.bads.distributionsystem.utils.Constants;
import com.sep490.bads.distributionsystem.utils.RemoteCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Log4j2
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final InMemoryOtpStore otpStore;
    private final MailService mailService;
    private final CacheKey cacheKey;
    private final RemoteCache remoteCache;

    @Value("${app.client-bads-url:http://localhost:3000}")
    private String clientPortalUrl;

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
        User u = userRepo.findActiveByEmail(email).orElse(null);
        if (u == null || u.getEmail() == null) return;

        String otp = otpStore.issue(u.getId()); // tạo OTP 6 số, TTL 5’
        String link = clientPortalUrl + "/reset-password-otp?otp=" + otp + "&email=" + u.getEmail();
        String html = """
            <p>Your OTP: <b>%s</b></p>
            <p>Click <a href="%s">here</a> to reset your password.</p>
        """.formatted(otp, link);

        mailService.sendMail(u.getEmail(), "Reset your password", html);
    }

    @Override
    @Transactional
    public void resetPasswordByOtp(String email, String otp, String newPassword) {
        User u = userRepo.findActiveByEmail(email)
                .orElseThrow(WrongAccountOrPasswordException::new);

        if (!otpStore.validate(u.getId(), otp)) {
            throw new IllegalArgumentException("Invalid OTP");
        }
        u.setPassword(passwordEncoder.encode(newPassword));
        userRepo.save(u);
        otpStore.clear(u.getId());
    }
}
