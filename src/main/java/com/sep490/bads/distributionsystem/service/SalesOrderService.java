package com.sep490.bads.distributionsystem.service;

import com.sep490.bads.distributionsystem.dto.SalesOrderCreateDto;
import com.sep490.bads.distributionsystem.dto.SalesOrderDto;
import com.sep490.bads.distributionsystem.dto.SalesOrderFilterDto;
import com.sep490.bads.distributionsystem.dto.SalesOrderUpdateDto;
import com.sep490.bads.distributionsystem.entity.SalesOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SalesOrderService {
    List<SalesOrderDto> getAllOrders();
    SalesOrder findById(Long id);
    SalesOrderDto createOrder(SalesOrderCreateDto dto);
    void updateOrder(Long id, SalesOrderUpdateDto dto);
    void softDeleteOrder(Long id);
    Page<SalesOrderDto> filterOrders(SalesOrderFilterDto filter, Pageable pageable);
}