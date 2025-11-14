package com.sep490.bads.distributionsystem.controller;

import com.sep490.bads.distributionsystem.dto.supplierDtos.*;
import com.sep490.bads.distributionsystem.entity.type.SupplierStatus;
import com.sep490.bads.distributionsystem.service.SupplierService;
import com.sep490.bads.distributionsystem.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/public/supplier")
@Tag(name = "Supplier", description = "quản lý nhà cung cấp")
@RequiredArgsConstructor
public class SupplierController extends BaseController {
    private final SupplierService supplierService;
    private final UserService userService;

    @Operation(summary="List + filter + paging")
    @GetMapping
    public ResponseEntity<Page<SupplierListItemDto>> getSuppliers(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) SupplierStatus status,
            @RequestParam(required = false) List<Long> categoryIds,
            @ParameterObject Pageable pageable) {

        SupplierFilterRequest filter = new SupplierFilterRequest();
        filter.setKeyword(keyword);
        filter.setStatus(status);
        filter.setCategoryIds(categoryIds);

        Page<SupplierListItemDto> page =
                supplierService.searchSuppliers(filter, pageable);

        return ResponseEntity.ok(page);
    }

    @Operation(summary="Create (popup Thêm nhà cung cấp)")
    @PostMapping
    public ResponseEntity<SupplierDetailInfoDto> createSupplier(
            @RequestBody CreateSupplierRequestDto dto, Authentication authentication) {

       Long currentUserId = getUserDetails(authentication).getUserId();
        SupplierDetailInfoDto result =
                supplierService.createSupplier(dto, currentUserId);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @Operation(summary="Detail - tab Thông tin")
    @GetMapping("/{id}")
    public ResponseEntity<SupplierDetailInfoDto> getSupplierDetail(
            @PathVariable Long id) {
        return ResponseEntity.ok(supplierService.getSupplierDetail(id));
    }

    @Operation(summary="Cập nhật thong tin nha cung cap")
    @PutMapping("/{id}")
    public ResponseEntity<SupplierDetailInfoDto> updateSupplier(
            @PathVariable Long id,
            @RequestBody UpdateSupplierRequestDto dto, Authentication authentication) {

        Long currentUserId = getUserDetails(authentication).getUserId();
        SupplierDetailInfoDto result =
                supplierService.updateSupplier(id, dto, currentUserId);
        return ResponseEntity.ok(result);
    }

    @Operation(summary="Đổi trạng thái (Đang hoạt động / Tạm ngưng)")
    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> changeStatus(
            @PathVariable Long id,
            @RequestParam SupplierStatus status) {
        supplierService.changeStatus(id, status);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary="Trang Giao dịch")
    @GetMapping("/{id}/transactions")
    public ResponseEntity<List<SupplierTransactionDto>> getTransactions(
            @PathVariable Long id) {
        return ResponseEntity.ok(supplierService.getSupplierTransactions(id));
    }

    @Operation(summary="Trang Thống kê")
    @GetMapping("/{id}/stats")
    public ResponseEntity<SupplierStatsDto> getStats(
            @PathVariable Long id) {
        return ResponseEntity.ok(supplierService.getSupplierStats(id));
    }
}
