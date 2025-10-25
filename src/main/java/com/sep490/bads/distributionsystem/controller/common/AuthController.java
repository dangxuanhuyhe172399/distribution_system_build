package com.sep490.bads.distributionsystem.controller.common;

import com.sep490.bads.distributionsystem.controller.BaseController;
import com.sep490.bads.distributionsystem.dto.ResetPasswordOtpDto;
import com.sep490.bads.distributionsystem.service.AuthService;
import com.sep490.bads.distributionsystem.dto.LoginDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/public/portal/auth")
@Tag(name = "Authentication", description = "")
public class AuthController extends BaseController {
    @Autowired
    private AuthService authService;

    @Operation(summary = "Đăng nhập")
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody LoginDto dto) {
        return ResponseEntity.ok(authService.signIn(dto));
    }

    @Operation(summary = "Quên mật khẩu (gửi link(có otp) reset qua mail)")
    @GetMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        authService.forgotPassword(email);
        return ResponseEntity.ok("Please check your email. If email existed, you will receive a link to reset your password.");
    }

    @Operation(summary = "Đặt lại mật khẩu (cần otp, mail của forgot password trong link gửi về gmail)")
    @PostMapping("/reset-password-otp")
    public ResponseEntity<?> resetPasswordByOtp(@Valid @RequestBody ResetPasswordOtpDto dto) {
        authService.resetPasswordByOtp(dto.getEmail(), dto.getOtp(), dto.getNewPassword());
        return ResponseEntity.ok("Password reset successfully");
    }

}

