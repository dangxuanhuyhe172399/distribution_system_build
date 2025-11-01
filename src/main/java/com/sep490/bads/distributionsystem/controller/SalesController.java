package com.sep490.bads.distributionsystem.controller;

import com.sep490.bads.distributionsystem.dto.*;
import com.sep490.bads.distributionsystem.dto.response.ApiResponse;
import com.sep490.bads.distributionsystem.entity.SalesOrder;
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

import java.util.List;

@RestController
@RequestMapping("/v1/public/sales-orders")
@RequiredArgsConstructor
@Validated
public class SalesController {

    private final SalesOrderService salesOrderService;

    @PostMapping("/createSaleOrder")
    public ResultResponse<SalesOrderDto> createSaleOrder(@Valid @RequestBody SalesOrderCreateDto body) {
        return ResultResponse.created(salesOrderService.createOrder(body));
    }

    @GetMapping("/{id}")
    public ResultResponse<SalesOrderDto> getSaleOrderById(@PathVariable @Positive Long id) {
        SalesOrder order = salesOrderService.findById(id);
        SalesOrderDto dto = new SalesOrderDto();
        dto.setId(order.getId());
        dto.setStatus(order.getStatus());
        dto.setPaymentMethod(order.getPaymentMethod());
        dto.setNote(order.getNote());
        return ResultResponse.success(dto);
    }

    @PutMapping("/{id}")
    public ResultResponse<Object> updateSaleOrder(@PathVariable @Positive Long id,
                                         @Valid @RequestBody SalesOrderUpdateDto dto) {
        salesOrderService.updateOrder(id, dto);
        return ResultResponse.success("Cập nhật đơn hàng thành công");
    }

    @DeleteMapping("/{id}")
    public ResultResponse<Object> softDeleteSaleOrder(@PathVariable @Positive Long id) {
        salesOrderService.softDeleteOrder(id);
        return ResultResponse.success("Đơn hàng đã được ẩn");
    }
    @GetMapping
    public ResponseEntity<ApiResponse<List<SalesOrderDto>>> getAllSaleOrders() {
        return ResponseEntity.ok(ApiResponse.success(salesOrderService.getAllOrders()));
    }
    @PostMapping("/filter")
    public ResultResponse<Page<SalesOrderDto>> filterSaleOrders(
            @Valid @RequestBody SalesOrderFilterDto filter, Pageable pageable) {
        return ResultResponse.success(salesOrderService.filterOrders(filter, pageable));
    }

}
