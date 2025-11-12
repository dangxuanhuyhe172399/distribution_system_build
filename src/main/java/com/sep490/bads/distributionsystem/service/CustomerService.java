package com.sep490.bads.distributionsystem.service;

import com.sep490.bads.distributionsystem.dto.customerDto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerService {
    Page<CustomersDto> search(Pageable pageable, CustomerFilterDto f);
    CustomersDto get(Long id);

    CustomersDto create(CustomerCreateDto dto, Long createdById);
    CustomersDto update(Long id, CustomerUpdateDto dto);

    void activate(Long id);
    void deactivate(Long id);
    void softDelete(Long id);
    CustomerInsightDto getInsight(Long customerId, int limit);
}
