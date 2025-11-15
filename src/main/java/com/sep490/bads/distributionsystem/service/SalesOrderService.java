package com.sep490.bads.distributionsystem.service;


import com.sep490.bads.distributionsystem.dto.salesOrderDtos.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SalesOrderService {
    Page<SalesOrderDto> search(Pageable pageable, SalesOrderFilterDto filter);

    SalesOrderDto get(Long id);

    SalesOrderDto createDraft(SalesOrderCreateDto dto, Long createdById);

    SalesOrderDto updateDraft(Long orderId, SalesOrderUpdateDto dto);

    SalesOrderDto submit(Long orderId);

    SalesOrderDto confirm(Long orderId);

    void cancel(Long orderId, String reason);
    OrderProgressDto getProgress(Long orderId);
    OrderProgressDto updateProgress(Long orderId, OrderProgressUpdateDto dto, Long userId);

    SalesOrderSummaryDto getDashboardSummary();

    SalesOrderSummaryDraffDto getDashboardDraffSummary();
}