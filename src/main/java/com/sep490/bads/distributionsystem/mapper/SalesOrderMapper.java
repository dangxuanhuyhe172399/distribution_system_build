package com.sep490.bads.distributionsystem.mapper;

import com.sep490.bads.distributionsystem.dto.salesOrderDtos.SalesOrderDto;
import com.sep490.bads.distributionsystem.entity.SalesOrder;
import org.mapstruct.*;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface SalesOrderMapper  extends EntityMapper<SalesOrderDto, SalesOrder> {
}
