package com.sep490.bads.distributionsystem.mapper;

import com.sep490.bads.distributionsystem.dto.*;
import com.sep490.bads.distributionsystem.entity.GoodsReceipt;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GoodsReceiptMapper extends EntityMapper<GoodsReceiptDto, GoodsReceipt> {
    @Override
    @Mapping(target = "contractId", source = "contract.id")
    @Mapping(target = "contractCode", source = "contract.contractCode")
    @Mapping(target = "warehouseId", source = "warehouse.id")
    @Mapping(target = "warehouseName", source = "warehouse.name")
    @Mapping(target = "createdById", source = "createdBy.id")
    @Mapping(target = "createdByName", source = "createdBy.fullName")
    @Mapping(target = "postedById", source = "postedBy.id")
    @Mapping(target = "postedByName", source = "postedBy.fullName")
    GoodsReceiptDto toDto(GoodsReceipt entity);
}
