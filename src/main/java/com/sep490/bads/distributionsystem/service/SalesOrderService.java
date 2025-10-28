package com.sep490.bads.distributionsystem.service;

import com.sep490.bads.distributionsystem.dto.SalesOrderCreateDto;
import com.sep490.bads.distributionsystem.dto.SalesOrderDto;
import com.sep490.bads.distributionsystem.dto.SalesOrderFilterDto;
import com.sep490.bads.distributionsystem.dto.SalesOrderStatusUpdateDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SalesOrderService {
    SalesOrderDto createOrder(SalesOrderCreateDto dto);
    SalesOrderDto getOrderById(Long id);
    SalesOrderDto updateStatus(Long id, SalesOrderStatusUpdateDto dto);
    void softDeleteOrder(Long id);
    Page<SalesOrderDto> filterOrders(SalesOrderFilterDto f);
    List<SalesOrderDto> getAllOrders();

}
