package com.sep490.bads.distributionsystem.service.impl;

import com.sep490.bads.distributionsystem.dto.*;
import com.sep490.bads.distributionsystem.entity.Product;
import com.sep490.bads.distributionsystem.mapper.ProductMapper;
import com.sep490.bads.distributionsystem.repository.ProductRepository;
import com.sep490.bads.distributionsystem.service.ProductService;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public ProductDto createProduct(ProductCreateDto dto) {
        if (productRepository.existsBySku(dto.getSku())) {
            throw new RuntimeException("SKU đã tồn tại: " + dto.getSku());
        }
        Product product = productMapper.toEntity(dto);
        Product saved = productRepository.save(product);
        return productMapper.toDto(saved);
    }

    @Override
    public ProductDto getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm ID: " + id));
        return productMapper.toDto(product);
    }

    @Override
    public ProductDto updateProduct(Long id, ProductUpdateDto dto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm ID: " + id));

        productMapper.updateEntityFromDto(dto, product);
        Product updated = productRepository.save(product);
        return productMapper.toDto(updated);
    }

    @Override
    public void softDeleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm ID: " + id));
        product.setStatus(false);
        productRepository.save(product);
    }

    @Override
    public Page<ProductDto> filterProducts(ProductFilterDto filterDto) {
        Sort.Direction direction = "DESC".equalsIgnoreCase(filterDto.getDirection())
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(
                filterDto.getPage(),
                filterDto.getSize(),
                Sort.by(direction, filterDto.getSortBy())
        );

        Specification<Product> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filterDto.getCategoryId() != null) {
                predicates.add(cb.equal(root.get("category").get("id"), filterDto.getCategoryId()));
            }
            if (filterDto.getKeyword() != null && !filterDto.getKeyword().trim().isEmpty()) {
                String like = "%" + filterDto.getKeyword().trim().toLowerCase() + "%";
                predicates.add(cb.like(cb.lower(root.get("name")), like));
            }
            if (filterDto.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), filterDto.getStatus()));
            }
            if (filterDto.getPriceFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("sellingPrice"), filterDto.getPriceFrom()));
            }
            if (filterDto.getPriceTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("sellingPrice"), filterDto.getPriceTo()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<Product> page = productRepository.findAll(spec, pageable);
        return page.map(productMapper::toDto);
    }
}
