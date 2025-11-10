package com.sep490.bads.distributionsystem.mapper;

import com.sep490.bads.distributionsystem.dto.invoiceDtos.InvoiceDto;
import com.sep490.bads.distributionsystem.entity.Invoice;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface InvoiceMapper extends EntityMapper<InvoiceDto, Invoice> {

}
