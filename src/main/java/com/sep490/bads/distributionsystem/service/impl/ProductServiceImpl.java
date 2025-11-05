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

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepo;
    private final ProductMapper productMapper;

    @Override
    @Transactional
    public ProductDto createProduct(ProductCreateDto dto) {
        if (productRepo.existsBySku(dto.getSku())) {
            throw new RuntimeException("SKU đã tồn tại: " + dto.getSku());
        }

        Product p = new Product();
        p.setSku(dto.getSku());
        p.setName(dto.getName());
        p.setCostPrice(dto.getCostPrice());
        p.setSellingPrice(dto.getSellingPrice());
        p.setMinStock(dto.getMinStock());
        p.setMaxStock(dto.getMaxStock());
        p.setStatus(CommonStatus.ACTIVE);

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
    @Transactional
    public ProductDto updateProduct(Long id, ProductUpdateDto dto) {
        Product p = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm ID: " + id));

        if (dto.getSku()!=null && !dto.getSku().equals(p.getSku()) &&
                productRepo.existsBySku(dto.getSku())) {
            throw new RuntimeException("SKU đã tồn tại: " + dto.getSku());
        }

        if (dto.getSku()!=null)         p.setSku(dto.getSku());
        if (dto.getName()!=null)        p.setName(dto.getName());
        if (dto.getCostPrice()!=null)   p.setCostPrice(dto.getCostPrice());
        if (dto.getSellingPrice()!=null)p.setSellingPrice(dto.getSellingPrice());
        if (dto.getStatus()!=null)      p.setStatus(dto.getStatus());
        if (dto.getMinStock()!=null) p.setMinStock(dto.getMinStock());
        if (dto.getMaxStock()!=null) p.setMaxStock(dto.getMaxStock());

        if (dto.getCategoryId()!=null || dto.getUnitId()!=null) {
            Long catId  = dto.getCategoryId()!=null ? dto.getCategoryId()
                    : (p.getCategory()!=null ? p.getCategory().getId() : null);
            Long unitId = dto.getUnitId()!=null ? dto.getUnitId()
                    : (p.getUnit()!=null ? p.getUnit().getId() : null);
            productRepo.updateRelations(p.getId(), catId, unitId);
        }

        Product updated = productRepo.save(p);
        return productMapper.toDto(updated);
    }

    @Override
    public void softDeleteProduct(Long id) {
        Product p = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm ID: " + id));

        // Chuyển trạng thái về INACTIVE (ẩn)
        p.setStatus(CommonStatus.INACTIVE);
        productRepo.save(p);

        log.info("Đã ẩn sản phẩm ID={} (soft delete)", id);
    }

    @Override
    public ProductDto recoverProduct(Long id) {
        Product p = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm ID: " + id));

        if (p.getStatus() == CommonStatus.ACTIVE) {
            throw new RuntimeException("Sản phẩm này đã đang ở trạng thái ACTIVE");
        }

        p.setStatus(CommonStatus.ACTIVE);
        productRepo.save(p);

        log.info("Khôi phục sản phẩm ID={} về trạng thái ACTIVE", id);
        return productMapper.toDto(p);
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
        return productRepo.findAll()
                .stream()
                .map(productMapper::toDto)
                .toList();
    }
}
