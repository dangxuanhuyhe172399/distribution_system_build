package com.sep490.bads.distributionsystem.dto.warehouseDtos;

import com.sep490.bads.distributionsystem.entity.type.CommonStatus;
import lombok.*;
import java.time.LocalDateTime;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WarehouseDto {
    private Long id;
    private String code;
    private String name;
    private String address;
    private String phone;
    private String email;
    private Boolean isActive;
    private CommonStatus status;

    private Long managerId;
    private String managerName;

    private LocalDateTime createdAt;
}
