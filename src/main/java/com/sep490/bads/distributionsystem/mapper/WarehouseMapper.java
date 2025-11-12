package com.sep490.bads.distributionsystem.mapper;

import com.sep490.bads.distributionsystem.dto.warehouseDto.WarehouseDto;
import com.sep490.bads.distributionsystem.entity.Warehouse;
import org.mapstruct.*;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public interface WarehouseMapper extends EntityMapper<WarehouseDto, Warehouse> {
}
