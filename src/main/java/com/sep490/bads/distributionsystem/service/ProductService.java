package com.sep490.bads.distributionsystem.service;

import com.sep490.bads.distributionsystem.dto.ProductCreateDto;
import com.sep490.bads.distributionsystem.dto.ProductDto;
import com.sep490.bads.distributionsystem.dto.ProductFilterDto;
import com.sep490.bads.distributionsystem.dto.ProductUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {

    ProductDto createProduct(ProductCreateDto productCreateDto);

    ProductDto getProductById(Long id);

    ProductDto updateProduct(Long id, ProductUpdateDto productUpdateDto);

    void softDeleteProduct(Long id);

    List<ProductDto> getAllProducts();

    Page<ProductDto> filterProducts(ProductFilterDto filterDto);

    public ProductDto recoverProduct(Long id) ;
}
