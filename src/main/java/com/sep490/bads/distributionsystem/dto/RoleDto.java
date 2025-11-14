package com.sep490.bads.distributionsystem.dto;
import com.sep490.bads.distributionsystem.entity.User;
import lombok.*;

import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class RoleDto {
    private Long roleId;
    private String roleName;
    private List<User> users;
}
