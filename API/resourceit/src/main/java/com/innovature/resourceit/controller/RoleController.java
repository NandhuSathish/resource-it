package com.innovature.resourceit.controller;

import com.innovature.resourceit.entity.dto.requestdto.RoleRequestDTO;
import com.innovature.resourceit.entity.dto.responsedto.RoleResponseDTO;
import com.innovature.resourceit.entity.dto.responsedto.SuccessResponseDTO;
import com.innovature.resourceit.exceptionhandler.BadRequestException;
import com.innovature.resourceit.service.RoleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Locale;

@RestController
@RequestMapping("/api/v1/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    MessageSource messageSource;

    public RoleController(RoleService roleService, MessageSource messageSource) {
        this.roleService = roleService;
        this.messageSource = messageSource;
    }

    @PostMapping
    public ResponseEntity<Object> addRole(@Valid @RequestBody RoleRequestDTO roleRequestDTO) {

        roleService.addRole(roleRequestDTO);
        String[] list = messageSource.getMessage("ROLE_ADDED", null, Locale.ENGLISH).split("-");
        return new ResponseEntity<>(new SuccessResponseDTO(list[0], list[1]), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Object> getRoles() {
        try {
            Collection<RoleResponseDTO> roles = roleService.getRoles();
            return new ResponseEntity<>(roles, HttpStatus.OK);
        } catch (Exception e) {
            throw new BadRequestException(messageSource.getMessage("ROLE_FETCHING_FAILED", null, Locale.ENGLISH));
        }
    }

    @GetMapping("/{roleId}")
    public ResponseEntity<Object> getRoleById(@PathVariable("roleId") Integer roleId) {
        RoleResponseDTO role = roleService.getRoleById(roleId);
        return new ResponseEntity<>(role, HttpStatus.OK);
    }

    @PutMapping("/{roleId}")
    public ResponseEntity<Object> updateRole(@PathVariable("roleId") Integer roleId,
                                             @Valid @RequestBody RoleRequestDTO roleRequestDTO) {
        roleService.updateRole(roleId, roleRequestDTO);
        String[] list = messageSource.getMessage("ROLE_UPDATED", null, Locale.ENGLISH).split("-");
        return new ResponseEntity<>(new SuccessResponseDTO(list[0], list[1]), HttpStatus.OK);
    }

    @DeleteMapping("/{roleId}")
    public ResponseEntity<Object> deleteRole(@PathVariable("roleId") Integer roleId) {
        roleService.deleteRole(roleId);
        String[] list = messageSource.getMessage("ROLE_DELETED", null, Locale.ENGLISH).split("-");
        return new ResponseEntity<>(new SuccessResponseDTO(list[0], list[1]), HttpStatus.OK);
    }

}
