package com.sep490.bads.distributionsystem.dto.warehouseDto;

import com.sep490.bads.distributionsystem.entity.type.CommonStatus;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WarehouseCreateDto {
    @NotBlank @Size(max=20)  private String code;
    @NotBlank @Size(max=100) private String name;
    @Size(max=255)           private String address;
    @Size(max=20)            private String phone;
    @Email @Size(max=100)    private String email;
    private Boolean isActive = true;
    private CommonStatus status = CommonStatus.ACTIVE;
    private Long managerId; // optional
}
