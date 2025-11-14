package com.sep490.bads.distributionsystem.service;

import com.sep490.bads.distributionsystem.dto.supplierDtos.*;
import com.sep490.bads.distributionsystem.entity.type.SupplierStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SupplierService {

    Page<SupplierListItemDto> searchSuppliers(SupplierFilterRequest filter, Pageable pageable);

    SupplierDetailInfoDto getSupplierDetail(Long id);

    SupplierDetailInfoDto createSupplier(CreateSupplierRequestDto dto, Long currentUserId);

    SupplierDetailInfoDto updateSupplier(Long id, UpdateSupplierRequestDto dto, Long currentUserId);

    void changeStatus(Long id, SupplierStatus status);

    List<SupplierTransactionDto> getSupplierTransactions(Long supplierId);

    SupplierStatsDto getSupplierStats(Long supplierId);
}

