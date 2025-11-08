package com.sep490.bads.distributionsystem.mapper;

import com.sep490.bads.distributionsystem.dto.customerDto.CustomersDto;
import com.sep490.bads.distributionsystem.entity.Customer;
import org.mapstruct.*;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface CustomerMapper extends EntityMapper<CustomersDto, Customer> {
}
