package com.innovature.resourceit.service;



import com.innovature.resourceit.entity.dto.requestdto.RoleRequestDTO;
import com.innovature.resourceit.entity.dto.responsedto.RoleResponseDTO;

import java.util.Collection;

public interface RoleService {

    void addRole(RoleRequestDTO roleRequestDTO);

    Collection<RoleResponseDTO> getRoles();

    void updateRole(Integer roleId, RoleRequestDTO roleRequestDTO);

    void deleteRole(Integer roleId);

    RoleResponseDTO getRoleById(Integer roleId);
}
