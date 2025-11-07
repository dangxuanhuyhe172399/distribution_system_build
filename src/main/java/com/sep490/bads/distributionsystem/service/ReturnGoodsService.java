package com.sep490.bads.distributionsystem.service;

import com.sep490.bads.distributionsystem.dto.ReturnGoodsDto.*;
import com.sep490.bads.distributionsystem.entity.*;
import org.springframework.transaction.annotation.Transactional;

public interface ReturnGoodsService {
    Request create(ReturnCreateDto dto, Long userId);
    Request inspect(Long requestId, ReturnInspectDto dto, Long userId);
    GoodsReceipt receipt(Long requestId, Long warehouseId, Long userId);
    GoodsIssues scrap(Long requestId, Long warehouseId, Long userId);

    ReturnGoodsDetailDto getReturnDetail(Long requestId);
}
