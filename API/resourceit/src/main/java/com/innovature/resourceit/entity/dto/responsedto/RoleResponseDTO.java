package com.innovature.resourceit.entity.dto.responsedto;

import com.innovature.resourceit.entity.Role;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoleResponseDTO {

    private Integer id;

    private String role;

    public RoleResponseDTO(Role role) {
        this.id = role.getId();
        this.role = role.getName();
    }
}
