package com.sep490.bads.distributionsystem.dto.purchaseOrderDtos;

import com.sep490.bads.distributionsystem.entity.type.ContractStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderFilterDto {
    private String search;                    // Tìm kiếm đơn mua hàn
    private Long supplierId;                  // Nhà cung cấp
    private Long createdById;                 // Người tạo
    private List<ContractStatus> statuses;    // Nháp / Chờ phê duyệt / ...
    private LocalDate fromDate;               // Từ ngày
    private LocalDate toDate;                 // Đến ngày
}
