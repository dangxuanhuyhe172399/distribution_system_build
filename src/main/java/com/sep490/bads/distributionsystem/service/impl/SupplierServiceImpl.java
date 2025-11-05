package com.sep490.bads.distributionsystem.service.impl;

import com.sep490.bads.distributionsystem.dto.*;
import com.sep490.bads.distributionsystem.entity.*;
import com.sep490.bads.distributionsystem.entity.type.CommonStatus;
import com.sep490.bads.distributionsystem.mapper.SupplierMapper;
import com.sep490.bads.distributionsystem.repository.SupplierCategoryRepository;
import com.sep490.bads.distributionsystem.repository.SupplierRepository;
import com.sep490.bads.distributionsystem.service.SupplierService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.criteria.Predicate;
import java.util.*;
@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;
    private final SupplierCategoryRepository supplierCategoryRepository;
    private final SupplierMapper supplierMapper;

    @Override
    public Page<SupplierDto> getAllSuppliers(Pageable pageable) {
        return supplierRepository.findAllActive(pageable).map(supplierMapper::toDto);
    }

    @Override
    public SupplierDto getSupplierById(Long id) {
        Supplier s = supplierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhà cung cấp ID=" + id));
        return supplierMapper.toDto(s);
    }

    @Override
    public SupplierDto createSupplier(SupplierCreateDto dto) {
        if (supplierRepository.existsByEmail(dto.getEmail()))
            throw new RuntimeException("Email đã tồn tại: " + dto.getEmail());
        if (supplierRepository.existsByPhone(dto.getPhone()))
            throw new RuntimeException("Số điện thoại đã tồn tại: " + dto.getPhone());

        Supplier entity = new Supplier();
        entity.setName(dto.getName());
        entity.setAddress(dto.getAddress());
        entity.setEmail(dto.getEmail());
        entity.setPhone(dto.getPhone());
        entity.setTaxCode(dto.getTaxCode());
        entity.setStatus(CommonStatus.ACTIVE);

        if (dto.getCategoryId() != null) {
            SupplierCategory category = supplierCategoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục nhà cung cấp ID=" + dto.getCategoryId()));
            entity.setCategory(category);
        }

        Supplier saved = supplierRepository.save(entity);
        log.info("Đã tạo nhà cung cấp mới: {}", saved.getName());
        return supplierMapper.toDto(saved);
    }

    @Override
    public SupplierDto updateSupplier(Long id, SupplierDto dto) {
        Supplier existing = supplierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhà cung cấp ID=" + id));

        existing.setName(dto.getName());
        existing.setAddress(dto.getAddress());
        existing.setEmail(dto.getEmail());
        existing.setPhone(dto.getPhone());
        existing.setTaxCode(dto.getTaxCode());
        existing.setStatus(dto.getStatus());

        if (dto.getCategoryId() != null) {
            SupplierCategory category = supplierCategoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục nhà cung cấp ID=" + dto.getCategoryId()));
            existing.setCategory(category);
        }

        Supplier updated = supplierRepository.save(existing);
        log.info("Đã cập nhật nhà cung cấp ID={}", id);
        return supplierMapper.toDto(updated);
    }

    @Override
    public void softDeleteSupplier(Long id) {
        Supplier s = supplierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhà cung cấp ID=" + id));

        s.setStatus(CommonStatus.INACTIVE);
        supplierRepository.save(s);
        log.info("Đã ẩn (soft delete) nhà cung cấp ID={}", id);
    }

    @Override
    public Page<SupplierDto> filterSuppliers(SupplierFilterDto f) {
        Pageable pageable = PageRequest.of(f.getPage(), f.getSize(),
                Sort.by("DESC".equalsIgnoreCase(f.getDirection()) ? Sort.Direction.DESC : Sort.Direction.ASC,
                        f.getSortBy()));

        Specification<Supplier> spec = (root, query, cb) -> {
            List<Predicate> ps = new ArrayList<>();

            if (f.getKeyword() != null && !f.getKeyword().isBlank()) {
                String like = "%" + f.getKeyword().trim().toLowerCase() + "%";
                ps.add(cb.or(
                        cb.like(cb.lower(root.get("name")), like),
                        cb.like(cb.lower(root.get("email")), like)
                ));
            }

            if (f.getCategoryId() != null)
                ps.add(cb.equal(root.get("category").get("id"), f.getCategoryId()));

            if (f.getStatus() != null)
                ps.add(cb.equal(root.get("status"), f.getStatus()));

            return cb.and(ps.toArray(new Predicate[0]));
        };

        return supplierRepository.findAll(spec, pageable).map(supplierMapper::toDto);
    }
    @Override
    public SupplierDto recover(Long id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhà cung cấp ID=" + id));

        if (supplier.getStatus() == CommonStatus.ACTIVE) {
            throw new RuntimeException("Nhà cung cấp này đã ở trạng thái ACTIVE rồi");
        }

        supplier.setStatus(CommonStatus.ACTIVE);
        Supplier recovered = supplierRepository.save(supplier);
        log.info("Đã khôi phục nhà cung cấp ID={}", id);

        return supplierMapper.toDto(recovered);
    }

}