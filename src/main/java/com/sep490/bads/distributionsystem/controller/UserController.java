package com.sep490.bads.distributionsystem.controller;

import com.sep490.bads.distributionsystem.dto.UserCreateDto;
import com.sep490.bads.distributionsystem.dto.UserDto;
import com.sep490.bads.distributionsystem.dto.UserUpdateDto;
import com.sep490.bads.distributionsystem.entity.User;
import com.sep490.bads.distributionsystem.entity.type.UserGender;
import com.sep490.bads.distributionsystem.entity.type.UserStatus;
import com.sep490.bads.distributionsystem.security.service.UserDetailsImpl;
import com.sep490.bads.distributionsystem.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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

    @Operation(summary = "Lấy danh sách users")
    @GetMapping("")
    public ResponseEntity<Page<User>> getAllUsers(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<User> users = userService.getAllUsers(pageable);
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Lấy chi tiết user theo id")
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserDetail(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @Operation(summary = "Tạo mới user")
    @PostMapping("")
    public ResponseEntity<UserDto> createUser(@RequestBody @Valid UserCreateDto dto) {
        UserDto created = userService.createUser(dto);
        return ResponseEntity.ok(created);
    }

    @Operation(summary = "Cập nhật user")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody @Valid UserUpdateDto updateData) {
        userService.updateUser(id, updateData);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Cập nhật status user")
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateUserStatus(@PathVariable Long id, @RequestBody UserStatus status) {
        userService.updateUserStatus(id, status);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Xóa mềm user (chuyển trạng thai)")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.softDeleteUser(id);
        return ResponseEntity.ok().build();
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
