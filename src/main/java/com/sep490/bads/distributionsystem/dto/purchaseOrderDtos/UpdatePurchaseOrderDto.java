package com.sep490.bads.distributionsystem.dto.purchaseOrderDtos;
import com.sep490.bads.distributionsystem.entity.type.ContractStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdatePurchaseOrderDto {
    private String note;
    private List<PurchaseOrderItemDto> items;
    private ContractStatus status; //  status
    //Lưu nháp/Gửi phê duyệt lại
}
