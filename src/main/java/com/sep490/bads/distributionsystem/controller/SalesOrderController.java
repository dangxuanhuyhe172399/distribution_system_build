package com.sep490.bads.distributionsystem.controller;

import com.sep490.bads.distributionsystem.dto.*;
import com.sep490.bads.distributionsystem.dto.response.ApiResponse;
import com.sep490.bads.distributionsystem.entity.SalesOrder;
import com.sep490.bads.distributionsystem.response.ResultResponse;
import com.sep490.bads.distributionsystem.service.SalesOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/public/sales-orders")
@RequiredArgsConstructor
@Validated
@Tag(name = "Sales Order", description = "Quản lý đơn hàng bán (SalesOrder)")
public class SalesOrderController {

    private final SalesOrderService salesOrderService;

    @Operation(summary = "Tạo đơn hàng mới", description = "Thêm một đơn hàng cùng danh sách sản phẩm (items).")
    @PostMapping
    public ResultResponse<SalesOrderDto> create(@Valid @RequestBody SalesOrderCreateDto body) {
        return ResultResponse.created(salesOrderService.createOrder(body));
    }

    @Operation(summary = "Lấy chi tiết đơn hàng", description = "Trả về thông tin chi tiết đơn hàng theo ID.")
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

    @Operation(summary = "Cập nhật thông tin đơn hàng", description = "Cập nhật các trường cơ bản như trạng thái, phương thức thanh toán, ghi chú,...")
    @PutMapping("/{id}")
    public ResultResponse<Object> update(@PathVariable @Positive Long id,
                                         @Valid @RequestBody SalesOrderUpdateDto dto) {
        salesOrderService.updateOrder(id, dto);
        return ResultResponse.success("Cập nhật đơn hàng thành công");
    }

    @Operation(summary = "Xóa mềm đơn hàng", description = "Đánh dấu trạng thái INACTIVE thay vì xóa vĩnh viễn.")
    @DeleteMapping("/{id}")
    public ResultResponse<Object> softDelete(@PathVariable @Positive Long id) {
        salesOrderService.softDeleteOrder(id);
        return ResultResponse.success("Đơn hàng đã được ẩn (INACTIVE)");
    }

    @Operation(summary = "Lấy danh sách tất cả đơn hàng", description = "Trả về toàn bộ danh sách đơn hàng (không phân trang).")
    @GetMapping
    public ResponseEntity<ApiResponse<List<SalesOrderDto>>> getAllOrders() {
        return ResponseEntity.ok(ApiResponse.success(salesOrderService.getAllOrders()));
    }

    @Operation(summary = "Lọc đơn hàng", description = "Lọc theo trạng thái, phương thức thanh toán, khoảng tổng tiền, từ khóa, có phân trang.")
    @PostMapping("/filter")
    public ResultResponse<Page<SalesOrderDto>> filterOrders(
            @Valid @RequestBody SalesOrderFilterDto filter, Pageable pageable) {
        return ResultResponse.success(salesOrderService.filterOrders(filter, pageable));
    }

}
