package com.sep490.bads.distributionsystem.service;

import com.sep490.bads.distributionsystem.dto.ReturnGoodsDtos.ReturnCreateDto;
import com.sep490.bads.distributionsystem.dto.ReturnGoodsDtos.ReturnGoodsDetailDto;
import com.sep490.bads.distributionsystem.dto.ReturnGoodsDtos.ReturnInspectDto;
import com.sep490.bads.distributionsystem.entity.GoodsIssues;
import com.sep490.bads.distributionsystem.entity.GoodsReceipt;
import com.sep490.bads.distributionsystem.entity.Request;

public interface ReturnGoodsService {
    Request create(ReturnCreateDto dto, Long userId);
    Request inspect(Long requestId, ReturnInspectDto dto, Long userId);
    GoodsReceipt receipt(Long requestId, Long warehouseId, Long userId);
    GoodsIssues scrap(Long requestId, Long warehouseId, Long userId);

    ReturnGoodsDetailDto getReturnDetail(Long requestId);
}
