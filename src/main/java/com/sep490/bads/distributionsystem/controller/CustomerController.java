package com.sep490.bads.distributionsystem.controller;

import com.sep490.bads.distributionsystem.dto.*;
import com.sep490.bads.distributionsystem.dto.response.ApiResponse;
import com.sep490.bads.distributionsystem.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/public/customers")
@RequiredArgsConstructor
@Tag(name = "Customer", description = "Quản lý thông tin khách hàng")
public class CustomerController extends BaseController {

    private final CustomerService customerService;


    @Operation(summary = "Lấy danh sách khách hàng (phân trang)")
    @PreAuthorize("hasRole('admin') or hasRole('saleStaff')")
    @GetMapping
    public ResponseEntity<Page<CustomerDto>> getAllCustomers(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(customerService.getAllCustomers(pageable));
    }


    @Operation(summary = "Lấy chi tiết khách hàng theo ID")
    @PreAuthorize("hasRole('admin') or hasRole('saleStaff')")
    @GetMapping("/{id}")
    public ResponseEntity<CustomerDto> getCustomerById(@PathVariable @Positive Long id) {
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

    @Operation(summary = "Tạo mới khách hàng")
    @PreAuthorize("hasRole('admin') or hasRole('saleStaff')")
    @PostMapping
    public ResponseEntity<CustomerDto> createCustomer(@RequestBody @Valid CustomerDto dto) {
        return ResponseEntity.ok(customerService.createCustomer(dto));
    }

    @Operation(summary = "Cập nhật thông tin khách hàng")
    @PutMapping("/{id}")
    public ResponseEntity<CustomerDto> updateCustomer(
            @PathVariable @Positive Long id,
            @RequestBody @Valid CustomerDto dto) {
        return ResponseEntity.ok(customerService.updateCustomer(id, dto));
    }


    @Operation(summary = "Xóa mềm khách hàng (chuyển trạng thái INACTIVE)")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> softDeleteCustomer(@PathVariable @Positive Long id) {
        customerService.softDeleteCustomer(id);
        return ResponseEntity.ok(ApiResponse.success("Đã ẩn (soft delete) khách hàng ID=" + id));
    }


    @Operation(summary = "Lọc khách hàng theo tên, loại, trạng thái, keyword (có phân trang)")
    @PostMapping("/filter")
    public ResponseEntity<Page<CustomerDto>> filterCustomers(@RequestBody CustomerFilterDto filter) {
        return ResponseEntity.ok(customerService.filterCustomers(filter));
    }
}
