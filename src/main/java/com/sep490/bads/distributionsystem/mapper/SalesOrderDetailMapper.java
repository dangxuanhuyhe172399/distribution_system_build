package com.sep490.bads.distributionsystem.mapper;

import com.sep490.bads.distributionsystem.dto.salesOrderDtos.SaleOrderDetailDto;
import com.sep490.bads.distributionsystem.entity.SalesOrderDetail;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface SalesOrderDetailMapper extends EntityMapper<SaleOrderDetailDto, SalesOrderDetail>{
}
