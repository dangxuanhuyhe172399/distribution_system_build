package com.sep490.bads.distributionsystem.controller;

import com.sep490.bads.distributionsystem.dto.salesOrderDtos.*;
import com.sep490.bads.distributionsystem.response.ResultResponse;
import com.sep490.bads.distributionsystem.service.SalesOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/public/sales")
@Tag(name = "SaleOrder", description = "quản lý đơn hàng và đơn hàng nháp")
@RequiredArgsConstructor
@Validated
public class SalesController extends BaseController {

    private final SalesOrderService service;

    @Operation(summary = "Tim kiem ")
    @GetMapping
    public ResultResponse<Page<SalesOrderDto>> searchSales(SalesOrderFilterDto filter, Pageable pageable) {
        return ResultResponse.success(service.search(pageable, filter));
    }

    @Operation(summary = "Xem chi tiêt đơn hang")
    @GetMapping("/{id}")
    public ResultResponse<SalesOrderDto> getById(@PathVariable Long id) {
        return ResultResponse.success(service.get(id));
    }

    @Operation(summary = "tạo đơn hàng  NHÁP")
    @PostMapping("/draft")
    public ResultResponse<SalesOrderDto> createDraft(@RequestBody @Valid SalesOrderCreateDto dto,
                                                     @RequestHeader(value = "X-User-Id") Long createdById) {
        return ResultResponse.success(service.createDraft(dto, createdById));
    }

    @Operation(summary = "Cập nhật khi NEW/PENDING (full-replace items)")
    @PutMapping("/{id}")
    public ResultResponse<SalesOrderDto> updateDraft(@PathVariable Long id,
                                                     @RequestBody @Valid SalesOrderUpdateDto dto) {
        return ResultResponse.success(service.updateDraft(id, dto));
    }

    @Operation(summary = " chuyển sang PENDING (gửi khách xác nhận)")
    @PostMapping("/{id}/submit")
    public ResultResponse<SalesOrderDto> submit(@PathVariable Long id) {
        return ResultResponse.success(service.submit(id));
    }

    @Operation(summary = "khách chốt → CONFIRMED + phát sinh mã đơn")
    @PostMapping("/{id}/confirm")
    public ResultResponse<SalesOrderDto> confirm(@PathVariable Long id) {
        return ResultResponse.success(service.confirm(id));
    }

    @Operation(summary = "huỷ đơn")
    @PostMapping("/{id}/cancel")
    public ResultResponse<Boolean> cancel(@PathVariable Long id, @RequestParam String reason) {
        service.cancel(id, reason);
        return ResultResponse.success(Boolean.TRUE);
    }

    @Operation(summary = "Xem tiến độ xử lý đơn hàng")
    @GetMapping("/{id}/progress")
    public ResultResponse<OrderProgressDto> getProgress(@PathVariable Long id) {
        return ResultResponse.success(service.getProgress(id));
    }

    // chỉ cho kế toán/kho chỉnh:
    @Operation(summary = "Cập nhật tiến độ xử lý đơn hàng (Kế toán/Kho)")
    @PutMapping("/{id}/progress")
    public ResultResponse<OrderProgressDto> updateProgress(
            @PathVariable Long id,
            @RequestBody @Valid OrderProgressUpdateDto dto,
            @RequestHeader("X-User-Id") Long userId) {
        return ResultResponse.success(service.updateProgress(id, dto, userId));
    }

    @Operation(summary = "Thống kê tổng quan đơn hàng")
    @GetMapping("/dashboard-summary")
    public ResultResponse<SalesOrderSummaryDto> getDashboardSummary() {
        return ResultResponse.success(service.getDashboardSummary());
    }

    @Operation(summary = "Thống kê tổng quan đơn hàng nháp")
    @GetMapping("/starcard-summary-draff")
    public ResultResponse<SalesOrderSummaryDraffDto> getDashboardSummaryDraff() {
        return ResultResponse.success(service.getDashboardDraffSummary());
    }
}
