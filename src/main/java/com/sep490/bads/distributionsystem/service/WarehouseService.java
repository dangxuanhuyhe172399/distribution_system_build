package com.sep490.bads.distributionsystem.service;

import com.sep490.bads.distributionsystem.dto.warehouseDto.WarehouseCreateDto;
import com.sep490.bads.distributionsystem.dto.warehouseDto.WarehouseDto;
import com.sep490.bads.distributionsystem.dto.warehouseDto.WarehouseFilterDto;
import com.sep490.bads.distributionsystem.dto.warehouseDto.WarehouseUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WarehouseService {
    Page<WarehouseDto> search(Pageable pageable, WarehouseFilterDto filter);
    WarehouseDto get(Long id);
    WarehouseDto create(WarehouseCreateDto dto);
    void update(Long id, WarehouseUpdateDto dto);
    void softDelete(Long id);            // set isActive=false, status=INACTIVE
    void activate(Long id);
    void deactivate(Long id);
}
