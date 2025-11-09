package com.sep490.bads.distributionsystem.service;


import com.sep490.bads.distributionsystem.dto.inventoryDtos.*;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InventoryService {
    Page<InventoryDto> listInventory(InventoryFilterDto filter, Pageable pageable);
    InventoryDto getById(Long id);

    // -------- GET ----------
    InventoryDto get(Long id);

    InventoryDto create(InventoryCreateDto dto);
    void update(Long id, InventoryUpdateDto dto);
    void delete(Long id);
    InventoryDetailDto getDetail(Long warehouseId, Long productId);
    Object history(Long productId, Long warehouseId);

    ByteArrayResource exportCsv(InventoryFilterDto filter);
}
