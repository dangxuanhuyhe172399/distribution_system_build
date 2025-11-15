package com.sep490.bads.distributionsystem.service.impl;

import com.sep490.bads.distributionsystem.dto.productDtos.ProductCreateDto;
import com.sep490.bads.distributionsystem.dto.productDtos.ProductDto;
import com.sep490.bads.distributionsystem.dto.productDtos.ProductFilterDto;
import com.sep490.bads.distributionsystem.dto.productDtos.ProductUpdateDto;
import com.sep490.bads.distributionsystem.entity.Product;
import com.sep490.bads.distributionsystem.entity.type.ProductStatus;
import com.sep490.bads.distributionsystem.mapper.ProductMapper;
import com.sep490.bads.distributionsystem.repository.InventoryRepository;
import com.sep490.bads.distributionsystem.repository.ProductCategoryRepository;
import com.sep490.bads.distributionsystem.repository.ProductRepository;
import com.sep490.bads.distributionsystem.repository.UnitRepository;
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
    private final ProductCategoryRepository categoryRepo;
    private final UnitRepository unitRepo;
    private final InventoryRepository inventoryRepo;
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
        p.setStatus(ProductStatus.ACTIVE);
        p.setDescription(dto.getDescription());
        p.setBarcode(dto.getBarcode());
        p.setImage(dto.getImage());
        p.setNote(dto.getNote());
        p.setReorderQty(dto.getReorderQty());

        p.setCategory(categoryRepo.getReferenceById(dto.getCategoryId()));
        p.setUnit(unitRepo.getReferenceById(dto.getUnitId()));

        productRepo.save(p);

        return toDtoWithExtra(p);
    }

    @Override
    public ProductDto getProductById(Long id) {
        Product p = productRepo.findByIdWithDetails(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm ID: " + id));

        return toDtoWithExtra(p);
    }

    @Override
    @Transactional
    public ProductDto updateProduct(Long id, ProductUpdateDto dto) {
        Product p = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm ID: " + id));

        if (dto.getSku()!=null && !dto.getSku().equals(p.getSku())
                && productRepo.existsBySku(dto.getSku())) {
            throw new RuntimeException("SKU đã tồn tại: " + dto.getSku());
        }

        if (dto.getSku()!=null)          p.setSku(dto.getSku());
        if (dto.getName()!=null)         p.setName(dto.getName());
        if (dto.getCostPrice()!=null)    p.setCostPrice(dto.getCostPrice());
        if (dto.getSellingPrice()!=null) p.setSellingPrice(dto.getSellingPrice());
        if (dto.getMinStock()!=null)     p.setMinStock(dto.getMinStock());
        if (dto.getMaxStock()!=null)     p.setMaxStock(dto.getMaxStock());
        if (dto.getStatus()!=null)       p.setStatus(dto.getStatus());
        if (dto.getDescription()!=null)  p.setDescription(dto.getDescription());
        if (dto.getBarcode()!=null)      p.setBarcode(dto.getBarcode());
        if (dto.getImage()!=null)        p.setImage(dto.getImage());
        if (dto.getNote()!=null)         p.setNote(dto.getNote());
        if (dto.getReorderQty()!=null)   p.setReorderQty(dto.getReorderQty());

        if (dto.getCategoryId()!=null)   p.setCategory(categoryRepo.getReferenceById(dto.getCategoryId()));
        if (dto.getUnitId()!=null)       p.setUnit(unitRepo.getReferenceById(dto.getUnitId()));

        return toDtoWithExtra(p);
    }


    @Override
    @Transactional
    public void softDeleteProduct(Long id) {
        Product p = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm: "));

        p.setStatus(ProductStatus.INACTIVE);
        productRepo.save(p);
    }

    @Override
    @Transactional
    public ProductDto recoverProduct(Long id) {
        Product p = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm ID: " + id));

        if (p.getStatus() == ProductStatus.ACTIVE) {
            throw new RuntimeException("Sản phẩm này đã đang ở trạng thái ACTIVE");
        }

        p.setStatus(ProductStatus.ACTIVE);
        productRepo.save(p);

        return toDtoWithExtra(p);
    }


    @Override
    @Transactional
    public Page<ProductDto> filterProducts(ProductFilterDto f) {
        Sort.Direction dir = "DESC".equalsIgnoreCase(f.getDirection())
                ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(f.getPage(), f.getSize(), Sort.by(dir, f.getSortBy()));

        return productRepo.findAll(ProductRepository.specFrom(f), pageable)
                .map(this::toDtoWithExtra);
    }

    @Override
    @Transactional
    public List<ProductDto> getAllProducts() {
        return productRepo.findAll()
                .stream()
                .map(this::toDtoWithExtra)
                .toList();
    }

    private ProductDto toDtoWithExtra(Product p) {
        ProductDto dto = productMapper.toDto(p);

        if (p.getCategory() != null) {
            dto.setCategoryId(p.getCategory().getId());
            dto.setCategoryName(p.getCategory().getName());
        }

        if (p.getUnit() != null) {
            dto.setUnitId(p.getUnit().getId());
            dto.setUnitName(p.getUnit().getName());
        }

        if (p.getId() != null) {
            dto.setStockQuantity(inventoryRepo.sumByProductId(p.getId()));
        }

        return dto;
    }
}
