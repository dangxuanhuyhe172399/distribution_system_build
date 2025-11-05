package com.sep490.bads.distributionsystem.controller;

import com.sep490.bads.distributionsystem.dto.inventoryDto.*;
import com.sep490.bads.distributionsystem.response.ResultResponse;
import com.sep490.bads.distributionsystem.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;

@RestController
@RequestMapping("v1/public/inventory")
@Tag(name = "Inventory", description = "Quản lý tồn kho")
@RequiredArgsConstructor
@Validated
public class InventoryController extends BaseController{
    private final InventoryService inventoryService;

    @Operation(summary = "Danh sách tồn kho (phân trang + lọc)")
    @GetMapping
    public ResultResponse<Page<InventoryDto>> listInventory(
            @RequestParam(required = false) Long warehouseId,
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Integer expireWithinDays,
            @PageableDefault(size = 10, sort = "product.id") Pageable pageable) {

        var f = InventoryFilterDto.builder()
                .warehouseId(warehouseId)
                .q(q)
                .status(status)
                .expireWithinDays(expireWithinDays)
                .build();
        return ResultResponse.success(inventoryService.listInventory(f, pageable));
    }

    @Operation(summary = "Chi tiết một dòng tồn kho")
    @GetMapping("/{id}")
    public ResultResponse<InventoryDto> getInventoryById(@PathVariable Long id) {
        return ResultResponse.success(inventoryService.getById(id));
    }

    @Operation(summary = "Tạo dòng tồn kho")
    @PostMapping
    public ResponseEntity<InventoryDto> create(@RequestBody @Valid InventoryCreateDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(inventoryService.create(dto));
    }

    @Operation(summary = "Cập nhật dòng tồn kho")
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody @Valid InventoryUpdateDto dto) {
        inventoryService.update(id, dto);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Xóa dòng tồn kho")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        inventoryService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Chi tiết tồn theo Sản phẩm–Kho")
    @GetMapping("/{warehouseId}/{productId}/detail")
    public ResultResponse<InventoryDetailDto> detail(@PathVariable Long warehouseId,
                                                     @PathVariable Long productId) {
        return ResultResponse.success(inventoryService.getDetail(warehouseId, productId));
    }


    @Operation(summary = "Lịch sử nhập/xuất theo sản phẩm-kho")
    @GetMapping("/{warehouseId}/{productId}/history")
    public ResultResponse<Object> history(@PathVariable Long warehouseId, @PathVariable Long productId) {
        return ResultResponse.success(inventoryService.history(productId, warehouseId));
    }

    @Operation(summary = "Xuất CSV")
    @GetMapping("/export")
    public ResponseEntity<Resource> export(
            @RequestParam(required = false) Long warehouseId,
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String status) {

        var f = InventoryFilterDto.builder().warehouseId(warehouseId).q(q).status(status).build();
        var file = inventoryService.exportCsv(f);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=inventory.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(file);
    }

}
