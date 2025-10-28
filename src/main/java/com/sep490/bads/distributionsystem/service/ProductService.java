package com.sep490.bads.distributionsystem.service;

import com.sep490.bads.distributionsystem.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface ProductService {

    ProductDto createProduct(ProductCreateDto productCreateDto);

    ProductDto getProductById(Long id);

    ProductDto updateProduct(Long id, ProductUpdateDto productUpdateDto);

    void softDeleteProduct(Long id);

    Page<ProductDto> filterProducts(ProductFilterDto filterDto);
}
