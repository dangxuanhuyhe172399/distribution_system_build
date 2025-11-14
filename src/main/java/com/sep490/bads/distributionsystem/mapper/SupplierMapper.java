package com.sep490.bads.distributionsystem.mapper;

import com.sep490.bads.distributionsystem.dto.supplierDtos.SupplierDto;
import com.sep490.bads.distributionsystem.entity.Supplier;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface SupplierMapper extends EntityMapper<SupplierDto,Supplier> {
}
