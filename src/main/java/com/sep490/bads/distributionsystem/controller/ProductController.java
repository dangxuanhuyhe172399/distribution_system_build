package com.sep490.bads.distributionsystem.controller;

import com.sep490.bads.distributionsystem.dto.ProductCreateDto;
import com.sep490.bads.distributionsystem.response.ResultResponse;
import com.sep490.bads.distributionsystem.dto.ProductDto;
import com.sep490.bads.distributionsystem.dto.ProductFilterDto;
import com.sep490.bads.distributionsystem.dto.ProductUpdateDto;
import com.sep490.bads.distributionsystem.dto.response.ApiResponse;
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
@Tag(name = "Product", description = "Quản lý sản phẩm")
@RequiredArgsConstructor
@Validated
public class ProductController extends BaseController {
    private final ProductService productService;

    @Operation(summary = "them san pham")
    @PostMapping
    public ResultResponse<ProductDto> createProduct(@Valid @RequestBody ProductCreateDto body) {
        return ResultResponse.created(productService.createProduct(body));
    }

    @Operation(summary = "Lay ra san pham theo Id")
    @GetMapping("/{id}")
    public ResultResponse<ProductDto> getProductById(@PathVariable @Positive Long id) {
        return ResultResponse.success(productService.getProductById(id));
    }

    @Operation(summary = "Cap nhat san pham moi")
    @PutMapping("/{id}")
    public ResultResponse<ProductDto> updateProduct(@PathVariable @Positive Long id,
                                                    @Valid @RequestBody ProductUpdateDto body) {
        return ResultResponse.success(productService.updateProduct(id, body));
    }

    @Operation(summary = "xoa san pham")
    @DeleteMapping("/{id}")
    public ResultResponse<Object> deleteProduct(@PathVariable @Positive Long id) {
        productService.softDeleteProduct(id);
        return ResultResponse.success(null);
    }

    @Operation(summary = "tim kiem san pham")
    @PostMapping("/filter")
    public ResultResponse<Page<ProductDto>> filterProduct(@Valid @RequestBody ProductFilterDto f) {
        return ResultResponse.success(productService.filterProducts(f));
    }

    @Operation(summary = "lay ra t ca san pham")
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductDto>>> getAllProducts() {
        return ResponseEntity.ok(
                ApiResponse.success(productService.getAllProducts())
        );
    }
    @PutMapping("/{id}/recover")
    public ResultResponse<ProductDto> recover(@PathVariable @Positive Long id) {
        return ResultResponse.success(productService.recoverProduct(id));
    }


}

