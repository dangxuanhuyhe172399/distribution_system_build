package com.sep490.bads.distributionsystem.controller;

import com.sep490.bads.distributionsystem.dto.UserCreateDto;
import com.sep490.bads.distributionsystem.dto.UserDto;
import com.sep490.bads.distributionsystem.dto.UserProfileUpdateDto;
import com.sep490.bads.distributionsystem.dto.UserUpdateDto;
import com.sep490.bads.distributionsystem.entity.User;
import com.sep490.bads.distributionsystem.entity.type.UserGender;
import com.sep490.bads.distributionsystem.entity.type.UserStatus;
import com.sep490.bads.distributionsystem.response.ResultResponse;
import com.sep490.bads.distributionsystem.security.service.UserDetailsImpl;
import com.sep490.bads.distributionsystem.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.hibernate.usertype.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@RestController
@RequestMapping("v1/public/user")
@Tag(name = "User", description = "User management")
public class UserController extends BaseController {
    @Autowired
    private UserService userService;

    @Operation(summary = "Lấy danh sách users")
    @GetMapping("")
    public ResultResponse<Page<UserDto>> getAllUsers(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResultResponse.success(userService.getAllUsers(pageable));
    }

    @Operation(summary = "Lấy chi tiết user theo id")
    @GetMapping("/{id}")
    public ResultResponse<UserDto> getUserDetail(@PathVariable Long id) {
        return ResultResponse.success(userService.findDtoById(id));
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
    public ResultResponse<UserDto> getProfile(Authentication authentication) {
        var me = getUserDetails(authentication);
        return ResultResponse.success(userService.getProfile(me.getUserId()));
    }
    @Operation(summary = "Cập nhật hồ sơ (không upload file)")
    @PutMapping("/updateProfile")
    public ResponseEntity<?> updateProfile(
            Authentication authentication,@RequestBody @Valid UserProfileUpdateDto dto) {
        var updateProfile = getUserDetails(authentication);
        userService.updateUserProfile(updateProfile, dto);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Upload avatar")
    @PutMapping(value = "/upload/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadAvatar(Authentication authentication,
                                             @RequestPart("file") MultipartFile file) {
        var uploadAvatar = getUserDetails(authentication);
        userService.updateUserAvatar(uploadAvatar, file);
        return ResponseEntity.accepted().build();
    }
}
