package com.innovature.resourceit.service.impli;

import com.innovature.resourceit.entity.Resource;
import com.innovature.resourceit.entity.Role;
import com.innovature.resourceit.entity.dto.requestdto.RoleRequestDTO;
import com.innovature.resourceit.entity.dto.responsedto.RoleResponseDTO;
import com.innovature.resourceit.exceptionhandler.BadRequestException;
import com.innovature.resourceit.repository.ResourceRepository;
import com.innovature.resourceit.repository.RoleRepository;
import com.innovature.resourceit.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.*;

@Service
public class RoleServiceImpli implements RoleService {

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    ResourceRepository resourceRepository;
    @Autowired
    MessageSource messageSource;

    private static final String ROLE_NOT_FOUND = "ROLE_NOT_FOUND";

    public RoleServiceImpli(RoleRepository roleRepository, ResourceRepository resourceRepository, MessageSource messageSource) {
        this.roleRepository = roleRepository;
        this.resourceRepository = resourceRepository;
        this.messageSource = messageSource;
    }

    @Override
    @Transactional
    public void addRole(RoleRequestDTO roleRequestDTO) {
        Optional<Role> role = roleRepository.findByName(roleRequestDTO.getRole().toUpperCase());
        role.ifPresent(value -> {
            throw new BadRequestException(messageSource.getMessage("ROLE_ALREADY_EXISTS", null, Locale.ENGLISH));
        });
        roleRepository.save(new Role(roleRequestDTO.getRole().toUpperCase()));
    }

    @Override
    public Collection<RoleResponseDTO> getRoles() {
        return StreamSupport.stream(roleRepository.findAllByIdNotOrderById(Resource.Roles.ADMIN.getId()).spliterator(), false).map(RoleResponseDTO::new).toList();
    }

    @Override
    public void updateRole(Integer roleId, RoleRequestDTO roleRequestDTO) {
        Role role = roleRepository.findById(roleId).orElseThrow(() -> new BadRequestException(messageSource.getMessage(ROLE_NOT_FOUND, null, Locale.ENGLISH)));
        Optional<Role> roleExists = roleRepository.findByName(roleRequestDTO.getRole().toUpperCase());
        roleExists.ifPresent(value -> {
            throw new BadRequestException(messageSource.getMessage("ROLE_ALREADY_EXISTS", null, Locale.ENGLISH));
        });
        role.setName(roleRequestDTO.getRole().toUpperCase());
        roleRepository.save(role);
    }

    @Override
    public void deleteRole(Integer roleId) {
        Optional<Role> role = roleRepository.findById(roleId);
        if (role.isEmpty()) {
            throw new BadRequestException(messageSource.getMessage(ROLE_NOT_FOUND, null, Locale.ENGLISH));

        }
        Optional<Resource> resource = resourceRepository.findByRoleId(roleId);
        resource.ifPresent(value -> {
            throw new BadRequestException(messageSource.getMessage("ROLE_CANNOT_DELETE", null, Locale.ENGLISH));
        });
        roleRepository.deleteById(roleId);
    }

    @Override
    public RoleResponseDTO getRoleById(Integer roleId) {
        return new RoleResponseDTO(roleRepository.findById(roleId).orElseThrow(() -> new BadRequestException(messageSource.getMessage(ROLE_NOT_FOUND, null, Locale.ENGLISH))));
    }
}
