package com.sep490.bads.distributionsystem.service.impl;

import com.sep490.bads.distributionsystem.dto.*;
import com.sep490.bads.distributionsystem.entity.Supplier;
import com.sep490.bads.distributionsystem.entity.type.SupplierStatus;
import com.sep490.bads.distributionsystem.exception.BadRequestException;
import com.sep490.bads.distributionsystem.exception.NotFoundException;
import com.sep490.bads.distributionsystem.repository.SupplierCategoryRepository;
import com.sep490.bads.distributionsystem.repository.SupplierRepository;
import com.sep490.bads.distributionsystem.repository.UserRepository;
import com.sep490.bads.distributionsystem.service.SupplierService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepo;
    private final SupplierCategoryRepository categoryRepo;
    private final UserRepository userRepo;

    // ---------- FILTER ----------
    @Override
    @Transactional
    public Page<SupplierDto> filter(SupplierFilterDto filterDto) {
        Pageable pageable = PageRequest.of(
                filterDto.getPage(),
                filterDto.getSize(),
                Sort.by(Sort.Direction.fromString(filterDto.getSortDirection()), filterDto.getSortBy())
        );
        return supplierRepo.findAll(SupplierRepository.specFrom(filterDto), pageable)
                .map(this::toDto);
    }

    // ---------- CREATE ----------
    @Override
    @Transactional
    public SupplierDto create(SupplierCreateDto dto) {
        validateUnique(dto.getPhone(), dto.getEmail(), null);

        var category = categoryRepo.findById(dto.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Danh mục nhà cung cấp không tồn tại"));

        var supplier = new Supplier();
        supplier.setName(dto.getName());
        supplier.setContactName(dto.getContactName());
        supplier.setPhone(dto.getPhone());
        supplier.setEmail(dto.getEmail());
        supplier.setAddress(dto.getAddress());
        supplier.setTaxCode(dto.getTaxCode());
        supplier.setCategory(category);
        supplier.setStatus(SupplierStatus.ACTIVE);

        supplier = supplierRepo.save(supplier);
        return toDto(supplier);
    }

    // ---------- UPDATE ----------
    @Override
    @Transactional
    public SupplierDto update(Long id, SupplierUpdateDto dto) {
        var supplier = supplierRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy nhà cung cấp"));

        validateUnique(dto.getPhone(), dto.getEmail(), supplier.getId());

        if (dto.getName() != null) supplier.setName(dto.getName());
        if (dto.getContactName() != null) supplier.setContactName(dto.getContactName());
        if (dto.getPhone() != null) supplier.setPhone(dto.getPhone());
        if (dto.getEmail() != null) supplier.setEmail(dto.getEmail());
        if (dto.getAddress() != null) supplier.setAddress(dto.getAddress());
        if (dto.getTaxCode() != null) supplier.setTaxCode(dto.getTaxCode());
        if (dto.getCategoryId() != null) {
            var category = categoryRepo.findById(dto.getCategoryId())
                    .orElseThrow(() -> new NotFoundException("Danh mục nhà cung cấp không tồn tại"));
            supplier.setCategory(category);
        }
        if (dto.getStatus() != null) supplier.setStatus(dto.getStatus());

        supplier = supplierRepo.save(supplier);
        return toDto(supplier);
    }

    // ---------- DELETE (SOFT DELETE) ----------
    @Override
    @Transactional
    public SupplierDto delete(Long id) {
        var supplier = supplierRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy nhà cung cấp"));
        supplier.setStatus(SupplierStatus.INACTIVE);
        supplier = supplierRepo.save(supplier);
        return toDto(supplier);
    }

    // ---------- RECOVER ----------
    @Override
    @Transactional
    public SupplierDto recover(Long id) {
        var supplier = supplierRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy nhà cung cấp"));
        supplier.setStatus(SupplierStatus.ACTIVE);
        supplier = supplierRepo.save(supplier);
        return toDto(supplier);
    }

    // ---------- DETAIL ----------
    @Override
    @Transactional
    public SupplierDto getDetailById(Long id) {
        var supplier = supplierRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy nhà cung cấp"));
        return toDto(supplier);
    }

    // ---------- TRANSACTIONS ----------
    @Override
    @Transactional
    public Object getTransactions(Long id, int page, int size) {
        // TODO: implement when PurchaseOrder module available
        return List.of();
    }

    // ---------- STATISTICS ----------
    @Override
    @Transactional
    public SupplierStatisticsDto getStatistics(Long id) {
        // TODO: implement when PurchaseOrder/Payment module available
        return SupplierStatisticsDto.builder()
                .totalOrders(0L)
                .totalDebt(0L)
                .totalPaid(0L)
                .build();
    }

    // ---------- EXPORT ----------
    @Override
    @Transactional
    public Resource exportSuppliers(String search, String status) {
        String csv = "ID,Name,Phone,Email,Status\n" +
                supplierRepo.findAll().stream()
                        .map(s -> s.getId() + "," +
                                s.getName() + "," +
                                s.getPhone() + "," +
                                s.getEmail() + "," +
                                s.getStatus())
                        .collect(Collectors.joining("\n"));
        return new ByteArrayResource(csv.getBytes());
    }

    // ---------- HELPERS ----------
    private void validateUnique(String phone, String email, Long exceptId) {
        if (StringUtils.hasText(phone)) {
            boolean dup = exceptId == null
                    ? supplierRepo.existsByPhone(phone)
                    : supplierRepo.existsByPhoneAndIdNot(phone, exceptId);
            if (dup) throw new BadRequestException("Số điện thoại đã tồn tại");
        }
        if (StringUtils.hasText(email)) {
            boolean dup = exceptId == null
                    ? supplierRepo.existsByEmail(email)
                    : supplierRepo.existsByEmailAndIdNot(email, exceptId);
            if (dup) throw new BadRequestException("Email đã tồn tại");
        }
    }

    private String generateSupplierCode() {
        var today = LocalDate.now();
        var start = today.atStartOfDay();
        var end = today.plusDays(1).atStartOfDay();
        long seq = supplierRepo.countByCreatedAtBetween(start, end) + 1;
        return "NCC" + today.format(DateTimeFormatter.BASIC_ISO_DATE) + "-" + String.format("%04d", seq);
    }

    private SupplierDto toDto(Supplier s) {
        var dto = new SupplierDto();
        dto.setId(s.getId());
        dto.setName(s.getName());
        dto.setContactName(s.getContactName());
        dto.setPhone(s.getPhone());
        dto.setEmail(s.getEmail());
        dto.setAddress(s.getAddress());
        dto.setTaxCode(s.getTaxCode());
        dto.setCategory(s.getCategory());
        dto.setStatus(s.getStatus());
        return dto;
    }
}
