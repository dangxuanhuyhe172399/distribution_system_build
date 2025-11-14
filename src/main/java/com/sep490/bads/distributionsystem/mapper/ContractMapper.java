package com.sep490.bads.distributionsystem.mapper;

import com.sep490.bads.distributionsystem.dto.purchaseOrderDtos.ContractDto;
import com.sep490.bads.distributionsystem.entity.Contract;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface ContractMapper extends EntityMapper<ContractDto,Contract> {
}
