package com.sep490.bads.distributionsystem.service;

import com.sep490.bads.distributionsystem.dto.stockDto.CreateIssueDto;
import com.sep490.bads.distributionsystem.dto.stockDto.CreateReceiptDto;
import com.sep490.bads.distributionsystem.entity.GoodsIssues;
import com.sep490.bads.distributionsystem.entity.GoodsReceipt;

public interface StockService {
    GoodsReceipt postReceipt(Long receiptId, Long userId);
    GoodsReceipt createAndPost(CreateReceiptDto dto, Long userId);

    GoodsIssues createIssueDraft(CreateIssueDto dto, Long userId);
    GoodsIssues confirmPick(Long issueId, Long userId);
    GoodsIssues postIssue(Long issueId, Long userId);
    GoodsIssues createAndPost(CreateIssueDto dto, Long userId);

}
