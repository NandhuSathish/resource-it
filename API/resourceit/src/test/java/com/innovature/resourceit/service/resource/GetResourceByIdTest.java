package com.innovature.resourceit.service.resource;

import com.innovature.resourceit.entity.Resource;
import com.innovature.resourceit.entity.dto.responsedto.ResourceResponseDTO;
import com.innovature.resourceit.exceptionhandler.BadRequestException;
import com.innovature.resourceit.repository.*;
import com.innovature.resourceit.service.impli.ResourceServiceImpli;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.test.context.ContextConfiguration;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
@ContextConfiguration(classes = GetResourceByIdTest.class)
class GetResourceByIdTest {
    @InjectMocks
    ResourceServiceImpli service;
    @Mock
    ResourceRepository resourceRepository;
    @Mock
    MessageSource messageSource;
    @Mock
    SkillRepository skillRepository;
    @Mock
    ResourceSkillRepository resourceSkillRepository;
    @Mock
    DepartmentRepository departmentRepository;
    @Mock
    RoleRepository roleRepository;

    @BeforeEach
    public void setUp() {
        service = new ResourceServiceImpli(messageSource, resourceRepository, skillRepository, resourceSkillRepository, departmentRepository, roleRepository);
    }

    @Test
    public void testGetResourceById() {
        int resourceId = 123;
        Resource resource = new Resource();
        resource.setId(resourceId);
        resource.setStatus(Resource.Status.ACTIVE.value);
        resource.setExperience(5);
        when(resourceRepository.findByIdAndStatus(eq(resourceId), eq(Resource.Status.ACTIVE.value)))
                .thenReturn(Optional.of(resource));

        when(messageSource.getMessage(eq("RESOURCE_NOT_FOUND"), eq(null), eq(Locale.ENGLISH)))
                .thenReturn("Resource not found");

        ResourceResponseDTO responseDTO = service.getResourceById(resourceId);

        verify(resourceRepository, times(1)).findByIdAndStatus(eq(resourceId), eq(Resource.Status.ACTIVE.value));

        assertNotNull(responseDTO);
        assertEquals(resourceId, responseDTO.getId());
    }

    @Test
    public void testGetResourceByIdResourceNotFound() {
        int resourceId = 123;
        when(resourceRepository.findByIdAndStatus(eq(resourceId), eq(Resource.Status.ACTIVE.value)))
                .thenReturn(Optional.empty());

        when(messageSource.getMessage(eq("RESOURCE_NOT_FOUND"), eq(null), eq(Locale.ENGLISH)))
                .thenReturn("Resource not found");

        assertThrows(BadRequestException.class, () -> service.getResourceById(resourceId));

        verify(resourceRepository, times(1)).findByIdAndStatus(eq(resourceId), eq(Resource.Status.ACTIVE.value));
    }

}
