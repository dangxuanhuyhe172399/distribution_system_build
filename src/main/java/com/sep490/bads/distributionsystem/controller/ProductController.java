package com.sep490.bads.distributionsystem.controller;

import com.sep490.bads.distributionsystem.dto.ProductCreateDto;
import com.sep490.bads.distributionsystem.dto.ProductDto;
import com.sep490.bads.distributionsystem.dto.ProductFilterDto;
import com.sep490.bads.distributionsystem.dto.ProductUpdateDto;
import com.sep490.bads.distributionsystem.dto.response.ApiResponse;
import com.sep490.bads.distributionsystem.response.ResultResponse;
import com.sep490.bads.distributionsystem.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/public/products")
@RequiredArgsConstructor
@Validated
@Tag(name = "Product", description = "Quản lý sản phẩm (Product)")
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "Tạo mới sản phẩm", description = "Thêm một sản phẩm mới vào hệ thống.")
    @PostMapping
    public ResultResponse<ProductDto> create(@Valid @RequestBody ProductCreateDto body) {
        return ResultResponse.created(productService.createProduct(body));
    }

    @Operation(summary = "Xem chi tiết sản phẩm", description = "Trả về thông tin chi tiết của sản phẩm theo ID.")
    @GetMapping("/{id}")
    public ResultResponse<ProductDto> get(@PathVariable @Positive Long id) {
        return ResultResponse.success(productService.getProductById(id));
    }

    @Operation(summary = "Cập nhật sản phẩm", description = "Cập nhật thông tin sản phẩm (tên, giá, tồn kho, danh mục, v.v...).")
    @PutMapping("/{id}")
    public ResultResponse<ProductDto> update(@PathVariable @Positive Long id,
                                             @Valid @RequestBody ProductUpdateDto body) {
        return ResultResponse.success(productService.updateProduct(id, body));
    }

    @Operation(summary = "Xóa mềm sản phẩm", description = "Ẩn sản phẩm khỏi danh sách hiển thị (soft delete).")
    @DeleteMapping("/{id}")
    public ResultResponse<Object> delete(@PathVariable @Positive Long id) {
        productService.softDeleteProduct(id);
        return ResultResponse.success(null);
    }

    @Operation(summary = "Lọc sản phẩm", description = "Lọc theo tên, danh mục, trạng thái, giá hoặc tồn kho, có phân trang.")
    @PostMapping("/filter")
    public ResultResponse<Page<ProductDto>> filter(@Valid @RequestBody ProductFilterDto f) {
        return ResultResponse.success(productService.filterProducts(f));
    }

    @Operation(summary = "Lấy toàn bộ danh sách sản phẩm", description = "Trả về tất cả sản phẩm (dạng danh sách).")
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductDto>>> getAllProducts() {
        return ResponseEntity.ok(ApiResponse.success(productService.getAllProducts()));
    }

    @Operation(summary = "Khôi phục sản phẩm đã ẩn", description = "Khôi phục lại sản phẩm bị soft delete.")
    @PutMapping("/{id}/recover")
    public ResultResponse<ProductDto> recover(@PathVariable @Positive Long id) {
        return ResultResponse.success(productService.recoverProduct(id));
    }
}

