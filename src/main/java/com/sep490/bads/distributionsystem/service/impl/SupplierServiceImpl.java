package com.sep490.bads.distributionsystem.service.impl;

import com.sep490.bads.distributionsystem.dto.supplierDtos.*;
import com.sep490.bads.distributionsystem.entity.Supplier;
import com.sep490.bads.distributionsystem.entity.SupplierCategory;
import com.sep490.bads.distributionsystem.entity.User;
import com.sep490.bads.distributionsystem.entity.type.SupplierStatus;
import com.sep490.bads.distributionsystem.exception.NotFoundException;
import com.sep490.bads.distributionsystem.repository.SupplierCategoryRepository;
import com.sep490.bads.distributionsystem.repository.SupplierRepository;
import com.sep490.bads.distributionsystem.repository.UserRepository;
import com.sep490.bads.distributionsystem.service.SupplierService;
import com.sep490.bads.distributionsystem.utils.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class SupplierServiceImpl implements SupplierService {
    private final SupplierRepository supplierRepository;
    private final SupplierCategoryRepository supplierCategoryRepository;
    private final UserRepository userRepository;

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // ========== LIST + FILTER ==========
    @Override
    public Page<SupplierListItemDto> searchSuppliers(SupplierFilterRequest filter, Pageable pageable) {

        Specification<Supplier> spec = Specification.where(null);

        // keyword: tìm theo tên, phone, taxCode
        if (StringUtils.hasText(filter.getKeyword())) {
            String kw = "%" + filter.getKeyword().trim().toLowerCase() + "%";
            spec = spec.and((root, query, cb) ->
                    cb.or(
                            cb.like(cb.lower(root.get("name")), kw),
                            cb.like(cb.lower(root.get("phone")), kw),
                            cb.like(cb.lower(root.get("taxCode")), kw)
                    )
            );
        }

        // status
        if (filter.getStatus() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("status"), filter.getStatus()));
        }

        // categoryIds
        if (!CollectionUtils.isEmpty(filter.getCategoryIds())) {
            spec = spec.and((root, query, cb) ->
                    root.get("category").get("id").in(filter.getCategoryIds()));
        }

        Page<Supplier> page = supplierRepository.findAll(spec, pageable);

        return page.map(this::mapToListItemDto);
    }

    private SupplierListItemDto mapToListItemDto(Supplier supplier) {
        BigDecimal debt = BigDecimal.ZERO; // TODO: tính từ đơn mua hàng + thanh toán sau

        return SupplierListItemDto.builder()
                .id(supplier.getId())
                .code(buildSupplierCode(supplier))
                .name(supplier.getName())
                .contactName(supplier.getContactName())
                .phone(supplier.getPhone())
                .taxCode(supplier.getTaxCode())
                .categoryName(
                        supplier.getCategory() != null
                                ? supplier.getCategory().getName()
                                : null
                )
                .outstandingDebt(debt)
                .status(supplier.getStatus())
                .build();
    }

    @Override
    public SupplierDetailInfoDto getSupplierDetail(Long id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Constants.SUPPLIER_NOT_FOUND));

        String createdAt = supplier.getCreatedAt() != null
                ? supplier.getCreatedAt().toLocalDate().format(DATE_FORMAT)
                : null;

        String createdByName = supplier.getCreatedBy() != null
                ? supplier.getCreatedBy().getFullName()
                : null;

        // TODO: sau này lấy từ Contract/Đơn mua hàng theo supplier_id
        String lastPurchaseDate = null;

        return SupplierDetailInfoDto.builder()
                .id(supplier.getId())
                .code(buildSupplierCode(supplier))
                .name(supplier.getName())
                .contactName(supplier.getContactName())
                .phone(supplier.getPhone())
                .email(supplier.getEmail())
                .address(supplier.getAddress())
                .taxCode(supplier.getTaxCode())
                .categoryName(
                        supplier.getCategory() != null
                                ? supplier.getCategory().getName()
                                : null
                )
                .status(supplier.getStatus())
                .paymentTermDays(supplier.getPaymentTermDays())
                .note(supplier.getNote())
                .createdByName(createdByName)
                .createdAt(createdAt)
                .lastPurchaseDate(lastPurchaseDate)
                .build();
    }

    @Override
    public SupplierDetailInfoDto createSupplier(CreateSupplierRequestDto dto, Long currentUserId) {

        SupplierCategory category = null;
        if (dto.getCategoryId() != null) {
            category = supplierCategoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() ->
                            new NotFoundException(Constants.SUPPLIER_CATEGORY_NOT_FOUND));
        }

        User creator = userRepository.findById(currentUserId)
                .orElseThrow(() -> new NotFoundException(Constants.USER_NOT_FOUND));

        Supplier supplier = Supplier.builder()
                .name(dto.getName())
                .contactName(dto.getContactName())
                .phone(dto.getPhone())
                .email(dto.getEmail())
                .address(dto.getAddress())
                .taxCode(dto.getTaxCode())
                .category(category)
                .status(SupplierStatus.ACTIVE)
                .paymentTermDays(dto.getPaymentTermDays())
                .note(dto.getNote())
                .createdBy(creator)
                .build();

        supplier = supplierRepository.save(supplier);

        // Sinh mã NCCxxx
        String code = generateSupplierCode(supplier.getId());
        supplier.setCode(code);
        supplier = supplierRepository.save(supplier);

        return getSupplierDetail(supplier.getId());
    }

    @Override
    public SupplierDetailInfoDto updateSupplier(Long id,
                                                UpdateSupplierRequestDto dto,
                                                Long currentUserId) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Constants.SUPPLIER_NOT_FOUND));

        if (dto.getName() != null) supplier.setName(dto.getName());
        supplier.setContactName(dto.getContactName());
        supplier.setPhone(dto.getPhone());
        supplier.setEmail(dto.getEmail());
        supplier.setAddress(dto.getAddress());
        supplier.setTaxCode(dto.getTaxCode());

        if (dto.getCategoryId() != null) {
            SupplierCategory category = supplierCategoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() ->
                            new NotFoundException(Constants.SUPPLIER_CATEGORY_NOT_FOUND));
            supplier.setCategory(category);
        }

        supplier.setPaymentTermDays(dto.getPaymentTermDays());
        supplier.setNote(dto.getNote());

        if (dto.getStatus() != null) {
            supplier.setStatus(dto.getStatus());
        }

        supplier = supplierRepository.save(supplier);
        return getSupplierDetail(supplier.getId());
    }

    @Override
    public void changeStatus(Long id, SupplierStatus status) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Constants.SUPPLIER_NOT_FOUND));
        supplier.setStatus(status);
        supplierRepository.save(supplier);
    }

    // GIAO DỊCH
    @Override
    public List<SupplierTransactionDto> getSupplierTransactions(Long supplierId) {
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new NotFoundException(Constants.SUPPLIER_NOT_FOUND));

        // TODO: sau này query từ Contract & Payment theo supplierId.
        // Hiện tại trả list rỗng để FE render được UI.
        return Collections.emptyList();
    }

    //  THỐNG KÊ
    @Override
    public SupplierStatsDto getSupplierStats(Long supplierId) {
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new NotFoundException(Constants.SUPPLIER_NOT_FOUND));

        // TODO: implement khi làm luồng Đơn mua hàng + thanh toán
        return SupplierStatsDto.builder()
                .totalPurchase(BigDecimal.ZERO)
                .outstandingDebt(BigDecimal.ZERO)
                .totalPaid(BigDecimal.ZERO)
                .onTimePaymentRate(BigDecimal.ZERO)
                .orderCount(0L)
                .avgOrderValue(BigDecimal.ZERO)
                .cooperationMonths(0L)
                .build();
    }

    // ========== HELPER ==========
    private String generateSupplierCode(Long id) {
        // NCC + 3 chữ số
        return String.format("NCC%03d", id);
    }

    private String buildSupplierCode(Supplier supplier) {
        if (supplier.getCode() != null) {
            return supplier.getCode();
        }
        if (supplier.getId() == null) return null;
        return generateSupplierCode(supplier.getId());
    }
}
