package com.sep490.bads.distributionsystem.dto.invoiceDtos;

import com.sep490.bads.distributionsystem.entity.type.DeliveryStatus;
import com.sep490.bads.distributionsystem.entity.type.InvoiceStatus;
import lombok.*;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class InvoiceFilterDto {
    private String keyword;
    private InvoiceStatus status;
    private DeliveryStatus deliveryStatus;
    private Instant from;
    private Instant to;
    private int page = 0;
    private int size = 10;
    private String sortBy = "id";
    private String direction = "DESC";
}
