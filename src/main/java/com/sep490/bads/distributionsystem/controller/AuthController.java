package com.tal.recruitment.system.controller;

import com.tal.recruitment.system.dto.request.AuthRequest;
import com.tal.recruitment.system.dto.response.AuthResponse;
import com.tal.recruitment.system.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest req) {
        return ResponseEntity.ok(authService.login(req));
    }

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }
}
