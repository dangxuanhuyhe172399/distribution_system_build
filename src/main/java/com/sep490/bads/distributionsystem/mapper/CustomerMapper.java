package com.sep490.bads.distributionsystem.mapper;

import com.sep490.bads.distributionsystem.dto.CustomerDto;
import com.sep490.bads.distributionsystem.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface CustomerMapper extends EntityMapper<CustomerDto, Customer> {

    @Mapping(source = "type.id", target = "typeId")
    @Mapping(source = "type.name", target = "typeName")
    CustomerDto toDto(Customer entity);

    @Mapping(source = "typeId", target = "type.id")
    Customer toEntity(CustomerDto dto);
}
