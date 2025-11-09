package com.sep490.bads.distributionsystem.mapper;

import com.sep490.bads.distributionsystem.dto.warehouseDtos.WarehouseDto;
import com.sep490.bads.distributionsystem.entity.Warehouse;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface WarehouseMapper extends EntityMapper<WarehouseDto, Warehouse> {
}
