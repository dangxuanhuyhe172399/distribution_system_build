package com.sep490.bads.distributionsystem.controller;

import com.sep490.bads.distributionsystem.dto.*;
import com.sep490.bads.distributionsystem.response.ResultResponse;
import com.sep490.bads.distributionsystem.service.SupplierService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/public/suppliers")
@RequiredArgsConstructor
@Tag(name = "Supplier", description = "Quản lý Nhà cung cấp")
@Validated
public class SupplierController {

    private final SupplierService supplierService;

    @Operation(summary = "Tạo nhà cung cấp mới")
    @PostMapping
    public ResultResponse<SupplierDto> create(@Valid @RequestBody SupplierCreateDto dto) {
        return ResultResponse.created(supplierService.create(dto));
    }

    @Operation(summary = "Lấy danh sách nhà cung cấp (lọc + phân trang)")
    @PostMapping("/filter")
    public ResponseEntity<Page<SupplierDto>> filter(@RequestBody SupplierFilterDto filterDto) {
        return ResponseEntity.ok(supplierService.filter(filterDto));
    }



    @Operation(summary = "Lấy chi tiết nhà cung cấp")
    @GetMapping("/{id}")
    public ResultResponse<SupplierDto> getDetail(@PathVariable Long id) {
        return ResultResponse.success(supplierService.getDetailById(id));
    }

    @Operation(summary = "Cập nhật nhà cung cấp")
    @PutMapping("/{id}")
    public ResultResponse<SupplierDto> update(@PathVariable Long id, @Valid @RequestBody SupplierUpdateDto dto) {
        return ResultResponse.success(supplierService.update(id, dto));
    }

    @Operation(summary = "Xóa (hoặc tạm ngưng) nhà cung cấp")
    @DeleteMapping("/{id}")
    public ResultResponse<SupplierDto> delete(@PathVariable Long id) {
        return ResultResponse.success(supplierService.delete(id));
    }

    @Operation(summary = "Khôi phục nhà cung cấp")
    @PutMapping("/{id}/recover")
    public ResultResponse<SupplierDto> recover(@PathVariable Long id) {
        return ResultResponse.success(supplierService.recover(id));
    }

    @Operation(summary = "Lấy lịch sử giao dịch của nhà cung cấp")
    @GetMapping("/{id}/transactions")
    public ResultResponse<Object> getTransactions(@PathVariable Long id,
                                                  @RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "10") int size) {
        return ResultResponse.success(supplierService.getTransactions(id, page, size));
    }

    @Operation(summary = "Lấy thống kê số liệu của nhà cung cấp")
    @GetMapping("/{id}/statistics")
    public ResultResponse<SupplierStatisticsDto> getStatistics(@PathVariable Long id) {
        return ResultResponse.success(supplierService.getStatistics(id));
    }

    @Operation(summary = "Xuất file danh sách nhà cung cấp (theo status định dạng CSV)")
    @GetMapping("/export")
    public ResponseEntity<Resource> exportSuppliers(@RequestParam(required = false) String search,
                                                    @RequestParam(required = false) String status) {
        Resource file = supplierService.exportSuppliers(search, status);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"suppliers.csv\"")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(file);
    }
//    @Operation(summary = "Lấy danh sách rút gọn (id, name)")
//    @GetMapping("/lookup")
//    public ResultResponse<List<SupplierLookupDto>> lookup() {
//        return ResultResponse.success(supplierService.lookup());
//    }
}
