package com.sep490.bads.distributionsystem.mapper;

import com.sep490.bads.distributionsystem.dto.SalesOrderDto;
import com.sep490.bads.distributionsystem.entity.SalesOrder;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface SalesOrderMapper  extends EntityMapper<SalesOrderDto, SalesOrder> {
}
