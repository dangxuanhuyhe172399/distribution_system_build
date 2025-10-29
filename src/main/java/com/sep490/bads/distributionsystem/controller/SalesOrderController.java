package com.sep490.bads.distributionsystem.controller;

import com.sep490.bads.distributionsystem.dto.*;
import com.sep490.bads.distributionsystem.dto.response.ApiResponse;
import com.sep490.bads.distributionsystem.entity.SalesOrder;
import com.sep490.bads.distributionsystem.repository.SalesOrderRepository;
import com.sep490.bads.distributionsystem.response.ResultResponse;
import com.sep490.bads.distributionsystem.service.SalesOrderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/sales-orders")
@RequiredArgsConstructor
@Validated
public class SalesOrderController {

    private final SalesOrderService salesOrderService;

    /** Tạo đơn hàng mới */
    @PostMapping
    public ResultResponse<SalesOrderDto> create(@Valid @RequestBody SalesOrderCreateDto body) {
        return ResultResponse.created(salesOrderService.createOrder(body));
    }

    /** Lấy chi tiết đơn hàng */
    @GetMapping("/{id}")
    public ResultResponse<SalesOrderDto> getById(@PathVariable @Positive Long id) {
        SalesOrder order = salesOrderService.findById(id);
        SalesOrderDto dto = new SalesOrderDto();
        dto.setId(order.getId());
        dto.setStatus(order.getStatus());
        dto.setPaymentMethod(order.getPaymentMethod());
        dto.setNote(order.getNote());
        return ResultResponse.success(dto);
    }

    /** Cập nhật thông tin đơn hàng */
    @PutMapping("/{id}")
    public ResultResponse<Object> update(@PathVariable @Positive Long id,
                                         @Valid @RequestBody SalesOrderUpdateDto dto) {
        salesOrderService.updateOrder(id, dto);
        return ResultResponse.success("Cập nhật đơn hàng thành công");
    }

    /** Xoá mềm đơn hàng */
    @DeleteMapping("/{id}")
    public ResultResponse<Object> softDelete(@PathVariable @Positive Long id) {
        salesOrderService.softDeleteOrder(id);
        return ResultResponse.success("Đơn hàng đã được ẩn");
    }
    /** Lấy toàn bộ danh sách */
    @GetMapping
    public ResponseEntity<ApiResponse<List<SalesOrderDto>>> getAllOrders() {
        return ResponseEntity.ok(ApiResponse.success(salesOrderService.getAllOrders()));
    }
    @PostMapping("/filter")
    public ResultResponse<Page<SalesOrderDto>> filterOrders(
            @Valid @RequestBody SalesOrderFilterDto filter, Pageable pageable) {
        return ResultResponse.success(salesOrderService.filterOrders(filter, pageable));
    }

}
