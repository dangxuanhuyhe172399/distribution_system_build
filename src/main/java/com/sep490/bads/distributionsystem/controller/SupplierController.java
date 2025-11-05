package com.sep490.bads.distributionsystem.controller;

import com.sep490.bads.distributionsystem.dto.*;
import com.sep490.bads.distributionsystem.dto.response.ApiResponse;
import com.sep490.bads.distributionsystem.response.ResultResponse;
import com.sep490.bads.distributionsystem.service.SupplierService;
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
@RequestMapping("v1/public/suppliers")
@RequiredArgsConstructor
@Validated
public class SupplierController {

    private final SupplierService supplierService;

    @PostMapping
    public ResultResponse<SupplierDto> create(@Valid @RequestBody SupplierCreateDto dto) {
        return ResultResponse.created(supplierService.createSupplier(dto));
    }

    @GetMapping("/{id}")
    public ResultResponse<SupplierDto> get(@PathVariable @Positive Long id) {
        return ResultResponse.success(supplierService.getSupplierById(id));
    }

    @PutMapping("/{id}")
    public ResultResponse<SupplierDto> update(@PathVariable @Positive Long id, @Valid @RequestBody SupplierDto dto) {
        return ResultResponse.success(supplierService.updateSupplier(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResultResponse<Object> delete(@PathVariable @Positive Long id) {
        supplierService.softDeleteSupplier(id);
        return ResultResponse.success("Nhà cung cấp đã được ẩn");
    }

    @PostMapping("/filter")
    public ResultResponse<Page<SupplierDto>> filter(@Valid @RequestBody SupplierFilterDto f) {
        return ResultResponse.success(supplierService.filterSuppliers(f));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<SupplierDto>>> getAllSuppliers() {
        return ResponseEntity.ok(ApiResponse.success(
                supplierService.getAllSuppliers(Pageable.unpaged()).getContent()
        ));
    }
    @PutMapping("/{id}/recover")
    public ResultResponse<SupplierDto> recover(@PathVariable @Positive Long id) {
        return ResultResponse.success(supplierService.recover(id));
    }

}


