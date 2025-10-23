package com.sep490.bads.distributionsystem.controller;

import com.sep490.bads.distributionsystem.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("bads/user")
@Tag(name = "User", description = "User management")
public class UserController extends BaseController {
    @Autowired
    private UserService userService;
    @Autowired
    private MediaService mediaService;

    @Operation(summary = "Đặt lại mật khẩu trong profile (đã đăng nhập)")
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(Authentication authentication, @Valid @RequestBody ResetPasswordDto dto) {
        UserDetailsImpl userDetails = getUserDetails(authentication);
        userService.resetPassword(userDetails, dto);
        return ResponseEntity.ok("Password reset successfully");
    }

    @Operation(summary = "Get profile user")
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(Authentication authentication) {
        UserDetailsImpl userDetails = getUserDetails(authentication);
        return ResponseEntity.ok(userService.getProfile(userDetails.getUserId()));
    }

    @Operation(summary = "Upload ảnh user lên Cloudflare R2")
    @PutMapping("/update")
    public ResponseEntity<String> uploadUserImage(
            Authentication authentication,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam(value = "birthday", required = false) String birthday,
            @RequestParam(value = "gender", required = false) UserGender gender) {

        UserDetailsImpl userDetails = getUserDetails(authentication);


        // Update user info (sync)
        userService.updateUserProfile(userDetails, file, birthday, gender);

        return ResponseEntity.accepted().body("Upload started");
    }

}
