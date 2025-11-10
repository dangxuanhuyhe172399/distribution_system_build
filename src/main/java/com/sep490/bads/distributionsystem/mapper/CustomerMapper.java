package com.sep490.bads.distributionsystem.mapper;

import com.sep490.bads.distributionsystem.dto.customerDtos.CustomersDto;
import com.sep490.bads.distributionsystem.entity.Customer;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface CustomerMapper extends EntityMapper<CustomersDto, Customer> {
}
