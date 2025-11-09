package com.sep490.bads.distributionsystem.dto.PickGoodsDtos;
import com.sep490.bads.distributionsystem.entity.type.StockNoteStatus;
import lombok.*;

import java.time.LocalDate;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PickGoodsHeaderDto {
    private String orderCode;
    private String customerName;
    private String email;
    private String phone;
    private String taxCode;
    private String address;
    private String paymentMethod;
    private String sellerName;
    private LocalDate orderDate;
    private StockNoteStatus status; // WAITING/CONFIRMED/POSTED...
}
