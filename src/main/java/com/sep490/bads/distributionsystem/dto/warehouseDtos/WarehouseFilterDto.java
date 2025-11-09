package com.sep490.bads.distributionsystem.dto.warehouseDtos;
import com.sep490.bads.distributionsystem.entity.type.CommonStatus;
import lombok.*;

@Data
@Getter
@Setter
public class WarehouseFilterDto {
    private String q;            // code/name/address
    private CommonStatus status; // ACTIVE/INACTIVE/...
    private Boolean isActive;    // true/false
    private Long managerId;
}
