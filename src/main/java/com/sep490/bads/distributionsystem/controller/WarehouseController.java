package com.sep490.bads.distributionsystem.controller;

import com.sep490.bads.distributionsystem.dto.warehouseDto.WarehouseCreateDto;
import com.sep490.bads.distributionsystem.dto.warehouseDto.WarehouseDto;
import com.sep490.bads.distributionsystem.dto.warehouseDto.WarehouseFilterDto;
import com.sep490.bads.distributionsystem.dto.warehouseDto.WarehouseUpdateDto;
import com.sep490.bads.distributionsystem.response.ResultResponse;
import com.sep490.bads.distributionsystem.service.WarehouseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/public/warehouse")
@RequiredArgsConstructor
@Tag(name = "Warehouse", description = "Quản lý hàng ở kho chứa")
public class WarehouseController extends BaseController {
    private final WarehouseService service;

    @Operation(summary = "Danh sách kho + lọc + phân trang")
    @GetMapping
    public ResultResponse<Page<WarehouseDto>> listWarehouse(WarehouseFilterDto filter,
                                                   @PageableDefault(size=10, sort="createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResultResponse.success(service.search(pageable, filter));
    }

    @Operation(summary = "Chi tiết kho")
    @GetMapping("/{id}")
    public ResultResponse<WarehouseDto> getWarehouseById(@PathVariable Long id) {
        return ResultResponse.success(service.get(id));
    }

    @Operation(summary = "Tạo kho")
    @PostMapping
    public ResponseEntity<WarehouseDto> create(@RequestBody @Valid WarehouseCreateDto dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @Operation(summary = "Cập nhật kho")
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody @Valid WarehouseUpdateDto dto) {
        service.update(id, dto);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Xóa mềm kho")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.softDelete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activate(@PathVariable Long id) {
        service.activate(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivate(@PathVariable Long id) {
        service.deactivate(id);
        return ResponseEntity.noContent().build();
    }
}
