package com.sep490.bads.distributionsystem.service;

import com.sep490.bads.distributionsystem.dto.PickGoodsDtos.PickGoodsDetailDto;
import com.sep490.bads.distributionsystem.dto.warehouseDtos.WarehouseCreateDto;
import com.sep490.bads.distributionsystem.dto.warehouseDtos.WarehouseDto;
import com.sep490.bads.distributionsystem.dto.warehouseDtos.WarehouseFilterDto;
import com.sep490.bads.distributionsystem.dto.warehouseDtos.WarehouseUpdateDto;
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
    PickGoodsDetailDto getPickDetailByOrder(Long orderId);
}
