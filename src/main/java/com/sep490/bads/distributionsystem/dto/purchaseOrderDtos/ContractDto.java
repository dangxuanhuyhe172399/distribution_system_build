package com.sep490.bads.distributionsystem.dto.purchaseOrderDtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sep490.bads.distributionsystem.entity.ContractDetail;
import com.sep490.bads.distributionsystem.entity.GoodsReceipt;
import com.sep490.bads.distributionsystem.entity.Supplier;
import com.sep490.bads.distributionsystem.entity.User;
import com.sep490.bads.distributionsystem.entity.type.CommonStatus;
import com.sep490.bads.distributionsystem.entity.type.ContractStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContractDto {
    private Long id;
    private String contractCode;
    private Supplier supplier;
    private User user;
    private ContractStatus status;
    private String note;
    private Boolean archived = false;
    private User createdBy;
    private List<ContractDetail> contractDetails;
    private List<GoodsReceipt> goodsReceipts;
}
