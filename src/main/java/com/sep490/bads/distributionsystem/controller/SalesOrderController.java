package com.sep490.bads.distributionsystem.controller;

import com.sep490.bads.distributionsystem.dto.*;
import com.sep490.bads.distributionsystem.response.ResultResponse;
import com.sep490.bads.distributionsystem.dto.response.ApiResponse;
import com.sep490.bads.distributionsystem.service.SalesOrderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/sales-orders")
@RequiredArgsConstructor
@Validated
public class SalesOrderController {

    private final SalesOrderService salesOrderService;

    /**
     * 🟢 1. Tạo mới đơn hàng
     */
    @PostMapping
    public ResultResponse<SalesOrderDto> create(@Valid @RequestBody SalesOrderCreateDto body) {
        return ResultResponse.created(salesOrderService.createOrder(body));
    }

    /**
     * 🟢 2. Lấy chi tiết đơn hàng theo ID
     */
    @GetMapping("/{id}")
    public ResultResponse<SalesOrderDto> getById(@PathVariable @Positive Long id) {
        return ResultResponse.success(salesOrderService.getOrderById(id));
    }

    /**
     * 🟢 3. Cập nhật trạng thái đơn hàng (CONFIRMED, CANCELED, COMPLETED)
     */
    @PutMapping("/{id}/status")
    public ResultResponse<SalesOrderDto> updateStatus(@PathVariable @Positive Long id,
                                                      @Valid @RequestBody SalesOrderStatusUpdateDto dto) {
        return ResultResponse.success(salesOrderService.updateStatus(id, dto));
    }

    /**
     * 🟢 4. Xóa mềm đơn hàng (ẩn khỏi danh sách)
     */
    @DeleteMapping("/{id}")
    public ResultResponse<Object> softDelete(@PathVariable @Positive Long id) {
        salesOrderService.softDeleteOrder(id);
        return ResultResponse.success(null);
    }

    /**
     * 🟢 5. Lấy danh sách đơn hàng có tìm kiếm / lọc / phân trang
     */
    @PostMapping("/filter")
    public ResultResponse<Page<SalesOrderDto>> filter(@Valid @RequestBody SalesOrderFilterDto f) {
        return ResultResponse.success(salesOrderService.filterOrders(f));
    }

    /**
     * 🟢 6. Lấy tất cả đơn hàng (không phân trang)
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<SalesOrderDto>>> getAllOrders() {
        return ResponseEntity.ok(ApiResponse.success(salesOrderService.getAllOrders()));
    }
}
