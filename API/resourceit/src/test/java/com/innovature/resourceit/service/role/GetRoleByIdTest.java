package com.innovature.resourceit.service.role;

import com.innovature.resourceit.entity.Role;
import com.innovature.resourceit.entity.dto.responsedto.RoleResponseDTO;
import com.innovature.resourceit.exceptionhandler.BadRequestException;
import com.innovature.resourceit.repository.ResourceRepository;
import com.innovature.resourceit.repository.RoleRepository;
import com.innovature.resourceit.service.impli.RoleServiceImpli;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.test.context.ContextConfiguration;

import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
@ContextConfiguration(classes = GetRoleByIdTest.class)
class GetRoleByIdTest {

    @Mock
    RoleRepository roleRepository;
    ;

    @Mock
    ResourceRepository resourceRepository;

    @Mock
    MessageSource messageSource;

    @InjectMocks
    RoleServiceImpli roleServiceImpli;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        roleServiceImpli = new RoleServiceImpli(roleRepository, resourceRepository, messageSource);
    }
    @Test
    void getRoleById_ExistingRole_ReturnsRoleResponseDTO() {
        int roleId = 1;
        when(roleRepository.findById(roleId)).thenReturn(Optional.of(new Role()));
        RoleResponseDTO roleResponseDTO = roleServiceImpli.getRoleById(roleId);

        assertNotNull(roleResponseDTO);
    }

    @Test
    void getRoleById_NonExistingRole_ThrowsBadRequestException() {
        int roleId = 1;
        when(roleRepository.findById(roleId)).thenReturn(Optional.empty());
        when(messageSource.getMessage("ROLE_NOT_FOUND", null, Locale.ENGLISH)).thenReturn("Role not found");

        assertThrows(BadRequestException.class, () -> roleServiceImpli.getRoleById(roleId));
    }
}