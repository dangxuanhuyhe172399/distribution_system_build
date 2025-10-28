package com.sep490.bads.distributionsystem.controller;

import com.sep490.bads.distributionsystem.dto.ProductCreateDto;
import com.sep490.bads.distributionsystem.response.ResultResponse;
import com.sep490.bads.distributionsystem.dto.ProductDto;
import com.sep490.bads.distributionsystem.dto.ProductFilterDto;
import com.sep490.bads.distributionsystem.dto.ProductUpdateDto;
import com.sep490.bads.distributionsystem.dto.response.ApiResponse;
import com.sep490.bads.distributionsystem.service.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Validated
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public ResultResponse<ProductDto> create(@Valid @RequestBody ProductCreateDto body) {
        return ResultResponse.created(productService.createProduct(body));
    }

    @GetMapping("/{id}")
    public ResultResponse<ProductDto> get(@PathVariable @Positive Long id) {
        return ResultResponse.success(productService.getProductById(id));
    }

    @PutMapping("/{id}")
    public ResultResponse<ProductDto> update(@PathVariable @Positive Long id,
                                             @Valid @RequestBody ProductUpdateDto body) {
        return ResultResponse.success(productService.updateProduct(id, body));
    }

    @DeleteMapping("/{id}")
    public ResultResponse<Object> delete(@PathVariable @Positive Long id) {
        productService.softDeleteProduct(id);
        return ResultResponse.success(null);
    }

    @PostMapping("/filter")
    public ResultResponse<Page<ProductDto>> filter(@Valid @RequestBody ProductFilterDto f) {
        return ResultResponse.success(productService.filterProducts(f));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductDto>>> getAllProducts() {
        return ResponseEntity.ok(
                ApiResponse.success(productService.getAllProducts())
        );
    }

}

