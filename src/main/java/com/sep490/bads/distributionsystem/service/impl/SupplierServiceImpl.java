package com.sep490.bads.distributionsystem.service.impl;

import com.sep490.bads.distributionsystem.dto.*;
import com.sep490.bads.distributionsystem.entity.Supplier;
import com.sep490.bads.distributionsystem.entity.type.CommonStatus;
import com.sep490.bads.distributionsystem.mapper.SupplierMapper;
import com.sep490.bads.distributionsystem.repository.SupplierCategoryRepository;
import com.sep490.bads.distributionsystem.repository.SupplierRepository;
import com.sep490.bads.distributionsystem.service.SupplierService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
public class SupplierServiceImpl implements SupplierService {

    private  SupplierRepository supplierRepository;
    private  SupplierCategoryRepository categoryRepository;

    private  SupplierMapper mapper;

    @Override
    public Page<SupplierDto> getAllSuppliers(Pageable pageable) {
        return null;
    }

    @Override
    public Page<SupplierDto> filter(SupplierFilterDto filterDto) {
        Pageable pageable = PageRequest.of(filterDto.getPage(), filterDto.getSize());
        return supplierRepository.findAll(SupplierRepository.specFrom(filterDto), pageable)
                .map(mapper::toDto);
    }

    @Override
    public SupplierDto create(SupplierCreateDto dto) {
        var category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Loại nhà cung cấp không tồn tại"));
        Supplier supplier = Supplier.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .address(dto.getAddress())
                .taxCode(dto.getTaxCode())
                .category(category)
                .status(CommonStatus.ACTIVE)
                .build();
        return mapper.toDto(supplierRepository.save(supplier));
    }

    @Override
    public SupplierDto update(Long id, SupplierUpdateDto dto) {
        var supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhà cung cấp"));
        if (dto.getName() != null) supplier.setName(dto.getName());
        if (dto.getEmail() != null) supplier.setEmail(dto.getEmail());
        if (dto.getPhone() != null) supplier.setPhone(dto.getPhone());
        if (dto.getAddress() != null) supplier.setAddress(dto.getAddress());
        if (dto.getTaxCode() != null) supplier.setTaxCode(dto.getTaxCode());
        if (dto.getCategoryId() != null)
            supplier.setCategory(categoryRepository.findById(dto.getCategoryId()).orElse(null));
        if (dto.getStatus() != null) supplier.setStatus(dto.getStatus());
        return mapper.toDto(supplierRepository.save(supplier));
    }

    @Override
    public SupplierDto delete(Long id) {
        var supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhà cung cấp"));
        supplier.setStatus(CommonStatus.INACTIVE);
        return mapper.toDto(supplierRepository.save(supplier));
    }

    @Override
    public SupplierDto recover(Long id) {
        var supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhà cung cấp"));
        supplier.setStatus(CommonStatus.ACTIVE);
        return mapper.toDto(supplierRepository.save(supplier));
    }

    @Override
    public SupplierDto  getDetailById(Long id) {
        var supplier = supplierRepository.findByIdFetchCategory(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhà cung cấp"));
        return SupplierDto.builder()
                .id(supplier.getId())
                .name(supplier.getName())
                .email(supplier.getEmail())
                .phone(supplier.getPhone())
                .address(supplier.getAddress())
                .taxCode(supplier.getTaxCode())
                .categoryName(supplier.getCategory().getName())
                .status(supplier.getStatus())
                .build();
    }


    @Override
    public Object getTransactions(Long id, int page, int size) {
        // TODO: implement logic when PurchaseOrder module available
        return List.of();
    }

    @Override
    public SupplierStatisticsDto getStatistics(Long id) {
        // TODO: implement when PurchaseOrder/Payment module available
        return SupplierStatisticsDto.builder()
                .totalOrders(0L)
                .totalDebt(0.0)
                .totalPaid(0.0)
                .build();
    }

    @Override
    public Resource exportSuppliers(String search, String status) {
        String csv = "ID,Name,Email,Phone,Status\n" +
                supplierRepository.findAll().stream()
                        .map(s -> s.getId() + "," + s.getName() + "," + s.getEmail() + "," + s.getPhone() + "," + s.getStatus())
                        .collect(Collectors.joining("\n"));
        return new ByteArrayResource(csv.getBytes());
    }

//    @Override
//    public Page<SupplierDto> getSuppliers(int page, int size, String search, String status, String type, String sort) {
//        Pageable pageable = PageRequest.of(page, size);
//        var f = SupplierFilterDto.builder()
//                .keyword(search)
//                .status(status != null ? CommonStatus.valueOf(status) : null)
//                .build();
//        return filter(f);
//    }

//    @Override
//    public List<SupplierLookupDto> lookup() {
//        return supplierRepository.findAllActive()
//                .stream()
//                .map(s -> new SupplierLookupDto(s.getId(), s.getName()))
//                .collect(Collectors.toList());
//    }
}
