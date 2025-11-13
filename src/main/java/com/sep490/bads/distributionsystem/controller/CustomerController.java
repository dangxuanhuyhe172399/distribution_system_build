package com.sep490.bads.distributionsystem.controller;

import com.sep490.bads.distributionsystem.dto.customerDtos.*;
import com.sep490.bads.distributionsystem.response.ResultResponse;
import com.sep490.bads.distributionsystem.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/public/customers")
@RequiredArgsConstructor
@Tag(name = "Customer", description = "Quản lý thông tin khách hàng")
public class CustomerController extends BaseController {

    private final CustomerService service;

    @Operation(summary = "Tìm kiếm + phân trang khách hàng")
    @GetMapping
    public ResultResponse<Page<CustomersDto>> searchByCustomer(CustomerFilterDto f, @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResultResponse.success(service.search(pageable, f));
    }

    @Operation(summary = "Xem chi tiết khách hàng")
    @GetMapping("/{id}")
    public ResultResponse<CustomersDto> getByIdCustomer(@PathVariable Long id) {
        return ResultResponse.success(service.get(id));
    }

    @Operation(summary = "Tạo khách hàng")
    @PostMapping
    public ResultResponse<CustomersDto> createCustomer(@RequestBody @Valid CustomerCreateDto dto,
                                               @RequestHeader(value = "X-User-Id", required = false) Long createdById) {
        return ResultResponse.created(service.create(dto, createdById));
    }

    @Operation(summary = "Cập nhật khách hàng")
    @PutMapping("/{id}")
    public ResultResponse<CustomersDto> updateCustomer(@PathVariable Long id, @RequestBody @Valid CustomerUpdateDto dto) {
        return ResultResponse.success(service.update(id, dto));
    }

    @Operation(summary = "Kích hoạt tài khoản khách hàng")
    @PostMapping("/{id}/activate")
    public ResultResponse<Void> activateCustomer(@PathVariable Long id) {
        service.activate(id);
        return ResultResponse.success(null);
    }

    @Operation(summary = "khoá tài khoản khách hàng")
    @PostMapping("/{id}/deactivate")
    public ResultResponse<Void> deactivateCustomer(@PathVariable Long id) {
        service.deactivate(id);
        return ResultResponse.success(null);
    }

    @Operation(summary = "Xoá mềm khách hàng")
    @DeleteMapping("/{id}")
    public ResultResponse<Void> softDelete(@PathVariable Long id) {
        service.softDelete(id);
        return ResultResponse.success(null);
    }
    @Operation(summary = "Thông tin tổng quan va lịch sử giao dịch khách hàng ")
    @GetMapping("/{id}/insights")
    public ResultResponse<CustomerInsightDto> insight(@PathVariable Long id,
                                                      @RequestParam(defaultValue = "10") int limit) {
        return ResultResponse.success(service.getInsight(id, limit));
    }
    @Operation(summary = "Top khách hàng mua hàng gần nhất (gợi ý cho tạo đơn)")
    @GetMapping("/recent")
    public ResultResponse<List<CustomerSuggestionDto>> recentCustomers(
            @RequestParam(defaultValue = "5") int limit
    ) {
        return ResultResponse.success(service.getRecentCustomers(limit));
    }
}
