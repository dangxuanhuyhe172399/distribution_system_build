package com.sep490.bads.distributionsystem.service;

import com.sep490.bads.distributionsystem.dto.*;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {

    ProductDto createProduct(ProductCreateDto productCreateDto);

    ProductDto getProductById(Long id);

    ProductDto updateProduct(Long id, ProductUpdateDto productUpdateDto);

    void softDeleteProduct(Long id);

    List<ProductDto> getAllProducts();

    Page<ProductDto> filterProducts(ProductFilterDto filterDto);

     ProductDto recoverProduct(Long id) ;
}
