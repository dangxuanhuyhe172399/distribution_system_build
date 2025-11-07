package com.sep490.bads.distributionsystem.service;

import com.sep490.bads.distributionsystem.dto.CustomerDto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerService {
    Page<CustomerDto> search(Pageable pageable, CustomerFilterDto f);
    CustomerDto get(Long id);

    CustomerDto create(CustomerCreateDto dto, Long createdById);
    CustomerDto update(Long id, CustomerUpdateDto dto);

    void activate(Long id);
    void deactivate(Long id);
    void softDelete(Long id);
    CustomerInsightDto getInsight(Long customerId, int limit);
}
