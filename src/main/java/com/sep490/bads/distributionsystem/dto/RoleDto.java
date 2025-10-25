package com.sep490.bads.distributionsystem.dto;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class RoleDto {
    private Long id;
    private String name;
    private String createdAt;
}
