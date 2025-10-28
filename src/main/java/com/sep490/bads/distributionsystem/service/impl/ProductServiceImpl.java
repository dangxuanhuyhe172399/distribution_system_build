package com.sep490.bads.distributionsystem.service.impl;

import com.sep490.bads.distributionsystem.dto.*;
import com.sep490.bads.distributionsystem.entity.Product;
import com.sep490.bads.distributionsystem.entity.type.CommonStatus;
import com.sep490.bads.distributionsystem.mapper.ProductMapper;
import com.sep490.bads.distributionsystem.repository.ProductRepository;
import com.sep490.bads.distributionsystem.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepo;
    private final ProductMapper productMapper;

    @Override
    public ProductDto createProduct(ProductCreateDto dto) {
        if (productRepo.existsBySku(dto.getSku())) {
            throw new RuntimeException("SKU đã tồn tại: " + dto.getSku());
        }
        // tạo entity thủ công (không dùng mapper)
        Product p = new Product();
        p.setSku(dto.getSku());
        p.setName(dto.getName());
        p.setCostPrice(dto.getCostPrice());
        p.setSellingPrice(dto.getSellingPrice());
        p.setStatus(CommonStatus.ACTIVE);
        p.setStockQuantity(0L);

        Product saved = productRepo.save(p);
        productRepo.updateRelations(saved.getId(), dto.getCategoryId(), dto.getUnitId());

        Product reloaded = productRepo.findById(saved.getId())
                .orElseThrow(() -> new IllegalStateException("Không tải lại được sản phẩm"));
        return productMapper.toDto(reloaded);
    }

    @Override
    public ProductDto getProductById(Long id) {
        Product p = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm ID: " + id));
        return productMapper.toDto(p);
    }

    @Override
    public ProductDto updateProduct(Long id, ProductUpdateDto dto) {
        Product p = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm ID: " + id));

        CommonStatus st = dto.getStatus() != null ? dto.getStatus() : p.getStatus();
        productRepo.updateBasic(
                p.getId(),
                dto.getSku() != null ? dto.getSku() : p.getSku(),
                dto.getName() != null ? dto.getName() : p.getName(),
                dto.getCostPrice() != null ? dto.getCostPrice() : p.getCostPrice(),
                dto.getSellingPrice() != null ? dto.getSellingPrice() : p.getSellingPrice(),
                st.name()
        );
        if (dto.getCategoryId() != null || dto.getUnitId() != null) {
            productRepo.updateRelations(
                    p.getId(),
                    dto.getCategoryId() != null ? dto.getCategoryId() : (p.getCategory()!=null ? p.getCategory().getId() : null),
                    dto.getUnitId() != null ? dto.getUnitId() : (p.getUnit()!=null ? p.getUnit().getId() : null)
            );
        }
        Product updated = productRepo.findById(id)
                .orElseThrow(() -> new IllegalStateException("Không tải lại được sản phẩm sau update"));
        return productMapper.toDto(updated);
    }

    @Override
    public void softDeleteProduct(Long id) {
        if (productRepo.softDelete(id) == 0) {
            throw new RuntimeException("Không tìm thấy sản phẩm ID: " + id);
        }
    }

    @Override
    public Page<ProductDto> filterProducts(ProductFilterDto f) {
        Sort.Direction dir = "DESC".equalsIgnoreCase(f.getDirection()) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(f.getPage(), f.getSize(), Sort.by(dir, f.getSortBy()));
        return productRepo.findAll(ProductRepository.specFrom(f), pageable)
                .map(productMapper::toDto);
    }

    @Override
    public List<ProductDto> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toDto)
                .toList();
    }

}
