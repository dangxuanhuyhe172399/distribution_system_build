package com.sep490.bads.distributionsystem.service.impl;

import com.sep490.bads.distributionsystem.dto.*;
import com.sep490.bads.distributionsystem.entity.*;
import com.sep490.bads.distributionsystem.entity.type.CommonStatus;
import com.sep490.bads.distributionsystem.mapper.CustomerMapper;
import com.sep490.bads.distributionsystem.repository.*;
import com.sep490.bads.distributionsystem.service.CustomerService;
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
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerTypeRepository customerTypeRepository;
    private final CustomerMapper customerMapper;

    @Override
    public Page<CustomerDto> getAllCustomers(Pageable pageable) {
        return customerRepository.findAllActive(pageable)
                .map(customerMapper::toDto);
    }

    @Override
    public CustomerDto getCustomerById(Long id) {
        Customer c = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng ID=" + id));
        return customerMapper.toDto(c);
    }

    @Override
    public CustomerDto createCustomer(CustomerDto dto) {
        if (customerRepository.existsByEmail(dto.getEmail()))
            throw new RuntimeException("Email đã tồn tại: " + dto.getEmail());
        if (customerRepository.existsByPhone(dto.getPhone()))
            throw new RuntimeException("Số điện thoại đã tồn tại: " + dto.getPhone());

        Customer entity = customerMapper.toEntity(dto);
        entity.setStatus(CommonStatus.ACTIVE);

        if (dto.getTypeId() != null) {
            CustomerType type = customerTypeRepository.findById(dto.getTypeId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy loại khách hàng ID=" + dto.getTypeId()));
            entity.setType(type);
        }

        Customer saved = customerRepository.save(entity);
        log.info("Đã tạo khách hàng mới: {}", saved.getName());
        return customerMapper.toDto(saved);
    }

    @Override
    public CustomerDto updateCustomer(Long id, CustomerDto dto) {
        Customer existing = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng ID=" + id));

        existing.setName(dto.getName());
        existing.setAddress(dto.getAddress());
        existing.setEmail(dto.getEmail());
        existing.setPhone(dto.getPhone());
        existing.setTaxCode(dto.getTaxCode());
        existing.setStatus(dto.getStatus());

        if (dto.getTypeId() != null) {
            CustomerType type = customerTypeRepository.findById(dto.getTypeId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy loại khách hàng ID=" + dto.getTypeId()));
            existing.setType(type);
        }

        Customer updated = customerRepository.save(existing);
        log.info("Đã cập nhật khách hàng ID={}", id);
        return customerMapper.toDto(updated);
    }

    @Override
    public void softDeleteCustomer(Long id) {
        Customer c = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng ID=" + id));

        c.setStatus(CommonStatus.INACTIVE);
        customerRepository.save(c);
        log.info("Đã ẩn (soft delete) khách hàng ID={}", id);
    }

    // ✅ Lọc khách hàng theo nhiều tiêu chí
    @Override
    public Page<CustomerDto> filterCustomers(CustomerFilterDto f) {
        Pageable pageable = PageRequest.of(f.getPage(), f.getSize(),
                Sort.by("DESC".equalsIgnoreCase(f.getDirection()) ? Sort.Direction.DESC : Sort.Direction.ASC,
                        f.getSortBy()));

        Specification<Customer> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (f.getKeyword() != null && !f.getKeyword().isBlank()) {
                String like = "%" + f.getKeyword().trim().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("name")), like),
                        cb.like(cb.lower(root.get("email")), like)
                ));
            }

            if (f.getTypeId() != null)
                predicates.add(cb.equal(root.get("type").get("id"), f.getTypeId()));

            if (f.getStatus() != null)
                predicates.add(cb.equal(root.get("status"), f.getStatus()));

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return customerRepository.findAll(spec, pageable).map(customerMapper::toDto);
    }
}
