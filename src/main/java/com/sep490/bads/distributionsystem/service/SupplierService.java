package com.sep490.bads.distributionsystem.service;

import com.sep490.bads.distributionsystem.dto.*;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface SupplierService {
    Page<SupplierDto> getAllSuppliers(Pageable pageable);

    Page<SupplierDto> filter(SupplierFilterDto filterDto);

    SupplierDto create(SupplierCreateDto dto);

    SupplierDto update(Long id, SupplierUpdateDto dto);

    SupplierDto delete(Long id);

    SupplierDto recover(Long id);

    SupplierDto  getDetailById(Long id);


    Object getTransactions(Long id, int page, int size);

    SupplierStatisticsDto getStatistics(Long id);

    Resource exportSuppliers(String search, String status);
}
