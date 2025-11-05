package com.sep490.bads.distributionsystem.service;

import com.sep490.bads.distributionsystem.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface SupplierService {
    Page<SupplierDto> getAllSuppliers(Pageable pageable);
    SupplierDto getSupplierById(Long id);
    SupplierDto createSupplier(SupplierCreateDto dto);
    SupplierDto updateSupplier(Long id, SupplierDto dto);
    void softDeleteSupplier(Long id);
    Page<SupplierDto> filterSuppliers(SupplierFilterDto f);
SupplierDto recover(Long id);
}
