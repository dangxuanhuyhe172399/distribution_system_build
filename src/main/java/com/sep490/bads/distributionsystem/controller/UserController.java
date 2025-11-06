package com.sep490.bads.distributionsystem.controller;

import com.sep490.bads.distributionsystem.dto.UserCreateDto;
import com.sep490.bads.distributionsystem.dto.UserDto;
import com.sep490.bads.distributionsystem.dto.UserProfileUpdateDto;
import com.sep490.bads.distributionsystem.dto.UserUpdateDto;
import com.sep490.bads.distributionsystem.dto.UserFilterDto;
import com.sep490.bads.distributionsystem.entity.type.UserStatus;
import com.sep490.bads.distributionsystem.response.ResultResponse;
import com.sep490.bads.distributionsystem.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("v1/public/user")
@Tag(name = "User", description = "User management")
public class UserController extends BaseController {
    @Autowired
    private UserService userService;

    @Operation(summary = "Lấy danh sách User và lọc danh sách")
    @PreAuthorize("hasRole('admin')")
    @GetMapping
    public ResultResponse<Page<UserDto>> getAllUserAndFilter(
            @ModelAttribute UserFilterDto filter,
            @PageableDefault(sort="createdAt", direction=Sort.Direction.DESC) Pageable pageable) {
        return ResultResponse.success(userService.search(pageable, filter));
    }

    @Operation(summary = "Lấy danh sách users")
    @PreAuthorize("hasRole('admin')")
    @GetMapping("/getAllUser")
    public ResultResponse<Page<UserDto>> getAllUsers(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResultResponse.success(userService.getAllUsers(pageable));
    }

    @Operation(summary = "Lấy chi tiết user theo id")
    @PreAuthorize("hasRole('admin')")
    @GetMapping("/{id}")
    public ResultResponse<UserDto> getUserDetail(@PathVariable Long id) {
        return ResultResponse.success(userService.findDtoById(id));
    }

    @Operation(summary = "Tạo mới user")
    @PreAuthorize("hasRole('admin')")
    @PostMapping("")
    public ResponseEntity<UserDto> createUser(@RequestBody @Valid UserCreateDto dto) {
        UserDto created = userService.createUser(dto);
        return ResponseEntity.ok(created);
    }

    @Operation(summary = "Cập nhật user")
    @PreAuthorize("hasRole('admin')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody @Valid UserUpdateDto updateData) {
        userService.updateUser(id, updateData);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Cập nhật status user")
    @PreAuthorize("hasRole('admin')")
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateUserStatus(@PathVariable Long id, @RequestBody UserStatus status) {
        userService.updateUserStatus(id, status);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Xóa mềm user (chuyển trạng thai)")
    @PreAuthorize("hasRole('admin')")
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

    @GetMapping("/exportData")
    public ResponseEntity<Resource> exportData(@ModelAttribute UserFilterDto filter) {
        ByteArrayResource file = userService.exportFile(filter);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=users.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(file);
    }
    @Operation(summary = "Đổi mật khẩu (trong profile)")
    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(
            Authentication authentication,
            @RequestParam String oldPassword,
            @RequestParam String newPassword) {
        var user = getUserDetails(authentication);
        userService.changePassword(user.getUserId(), oldPassword, newPassword);
        return ResponseEntity.ok("Password changed successfully");
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activate(@PathVariable Long id){
        userService.updateUserStatus(id, UserStatus.ACTIVE);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivate(@PathVariable Long id){
        userService.updateUserStatus(id, UserStatus.INACTIVE);
        return ResponseEntity.noContent().build();
    }

}
