package com.sep490.bads.distributionsystem.service;

import com.sep490.bads.distributionsystem.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GoodsReceiptService {
    Page<GoodsReceiptDto> search(Pageable pageable, GoodsReceiptFilterDto filter);
    GoodsReceiptDto findById(Long id);
    GoodsReceiptDto create(GoodsReceiptCreateDto dto, Long userId);
    void postReceipt(Long id, Long userId);
}
