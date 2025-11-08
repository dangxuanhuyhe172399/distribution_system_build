package com.sep490.bads.distributionsystem.dto.ReturnGoodsDto;

import com.sep490.bads.distributionsystem.entity.type.RequestStatus;
import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ReturnGoodsHeaderDto {
    private String requestCode;
    private String requesterName;     // người trả
    private String sellerName;        // NV bán hàng
    private LocalDate returnDate;
    private String org;               // đơn vị trả
    private String reason;
    private RequestStatus status;            // NEW/INSPECTING/APPROVED/REJECTED/RECEIPTED
}
