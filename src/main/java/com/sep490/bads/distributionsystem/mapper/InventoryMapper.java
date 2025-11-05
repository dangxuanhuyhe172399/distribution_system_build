package com.sep490.bads.distributionsystem.mapper;

import com.sep490.bads.distributionsystem.dto.inventoryDto.InventoryDto;
import com.sep490.bads.distributionsystem.entity.Inventory;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface InventoryMapper extends EntityMapper<InventoryDto, Inventory> {
}
