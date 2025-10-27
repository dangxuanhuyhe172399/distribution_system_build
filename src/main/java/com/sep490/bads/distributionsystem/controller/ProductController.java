package com.sep490.bads.distributionsystem.controller;

import com.sep490.bads.distributionsystem.dto.ProductCreateDto;
import com.sep490.bads.distributionsystem.dto.ProductDto;
import com.sep490.bads.distributionsystem.dto.ProductFilterDto;
import com.sep490.bads.distributionsystem.dto.ProductUpdateDto;
import com.sep490.bads.distributionsystem.dto.response.ApiResponse;
import com.sep490.bads.distributionsystem.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    //     Create product
    @PostMapping
    public ResponseEntity<ApiResponse<ProductDto>> createProduct(
            @Valid @RequestBody ProductCreateDto productCreateDto) {
        return ResponseEntity.ok(
                ApiResponse.success(productService.createProduct(productCreateDto))
        );
    }

    //Get detail by ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductDto>> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.success(productService.getProductById(id))
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductDto>> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductUpdateDto productUpdateDto) {
        return ResponseEntity.ok(
                ApiResponse.success(productService.updateProduct(id, productUpdateDto))
        );
    }


    //    Soft delete (status = false)
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long id) {
        productService.softDeleteProduct(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    // Filter + Pagination (POST vì nhận object phức tạp + body)
    @PostMapping("/filter")
    public ResponseEntity<ApiResponse<Page<ProductDto>>> filterProducts(
            @RequestBody ProductFilterDto filterDto) {
        return ResponseEntity.ok(
                ApiResponse.success(productService.filterProducts(filterDto))
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductDto>>> getAllProducts() {
        return ResponseEntity.ok(
                ApiResponse.success(productService.getAllProducts())
        );
    }

}
