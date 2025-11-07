package com.sep490.bads.distributionsystem.service;

import com.sep490.bads.distributionsystem.dto.CustomerDto;
import com.sep490.bads.distributionsystem.dto.CustomerFilterDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerService {
    Page<CustomerDto> getAllCustomers(Pageable pageable);
    CustomerDto getCustomerById(Long id);
    CustomerDto createCustomer(CustomerDto dto);
    CustomerDto updateCustomer(Long id, CustomerDto dto);
    void softDeleteCustomer(Long id);
    Page<CustomerDto> filterCustomers(CustomerFilterDto f);
}
