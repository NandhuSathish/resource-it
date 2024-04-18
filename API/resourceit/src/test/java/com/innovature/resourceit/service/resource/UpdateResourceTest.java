package com.innovature.resourceit.service.resource;

import com.innovature.resourceit.entity.*;
import com.innovature.resourceit.entity.dto.requestdto.ResourceRequestDTO;
import com.innovature.resourceit.entity.dto.requestdto.ResourceSkillRequestDTO;
import com.innovature.resourceit.exceptionhandler.BadRequestException;
import com.innovature.resourceit.repository.*;
import com.innovature.resourceit.service.impli.ResourceServiceImpli;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.test.context.ContextConfiguration;

import java.util.Date;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@ContextConfiguration(classes = UpdateResourceTest.class)
class UpdateResourceTest {

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
    @Mock
    AllocationRepository allocationRepository;

    private static final String RESOURCE_NOT_FOUND = "resource.not.found";

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUpdateResourceFoundAndEmailSame() {
        int resourceId = 123;
        ResourceRequestDTO dto = createResourceRequestDTO();
        Department department = new Department();
        Role role = new Role();
        role.setId(1);
        role.setName("HR");
        department.setDepartmentId(1);
        Skill validSkill = new Skill();
        validSkill.setId(dto.getSkills().iterator().next().getSkillId());
        when(skillRepository.findById(dto.getSkills().iterator().next().getSkillId())).thenReturn(Optional.of(validSkill));
        Resource resource = createResource(resourceId, dto.getEmail(), true);
        resource.setRole(role);
        when(resourceRepository.findByIdAndStatus(eq(resourceId), eq(Resource.Status.ACTIVE.value))).thenReturn(Optional.of(resource));
        when(resourceRepository.findByEmailIgnoreCase(dto.getEmail())).thenReturn(Optional.empty());
        when(departmentRepository.findById(dto.getDepartmentId())).thenReturn(Optional.of(department));
        when(roleRepository.findById(dto.getRole())).thenReturn(Optional.of(role));
        when(allocationRepository.findTopByResourceIdAndProjectProjectTypeNotAndAllocationExpiryAndStatusOrderByEndDateDesc(anyInt(), anyByte(), anyByte(), anyByte())).thenReturn(Optional.empty());
        service.updateResource(resourceId, dto);
        verify(resourceRepository, times(1)).save(resource);
    }

    @Test
    void testUpdateResourceFoundAndEmailDifferent() {
        int resourceId = 123;
        Department department = new Department();
        Role role = new Role();
        role.setId(1);
        role.setName("HR");
        department.setDepartmentId(1);
        ResourceRequestDTO dto = createResourceRequestDTO();
        Skill validSkill = new Skill();
        validSkill.setId(dto.getSkills().iterator().next().getSkillId());
        dto.setJoiningDate(new Date());
        when(skillRepository.findById(dto.getSkills().iterator().next().getSkillId())).thenReturn(Optional.of(validSkill));

        Resource resource = createResource(resourceId, "different-email@example.com", true);
        resource.setRole(role);
        when(resourceRepository.findByIdAndStatus(eq(resourceId), eq(Resource.Status.ACTIVE.value))).thenReturn(Optional.of(resource));
        when(resourceRepository.findByEmailIgnoreCase(dto.getEmail())).thenReturn(Optional.empty());
        when(resourceRepository.findByEmployeeId(dto.getEmployeeId())).thenReturn(Optional.empty());
        when(departmentRepository.findById(dto.getDepartmentId())).thenReturn(Optional.of(department));
        when(roleRepository.findById(dto.getRole())).thenReturn(Optional.of(role));

        service.updateResource(resourceId, dto);
        verify(resourceRepository, times(1)).save(resource);
    }

    @Test
    void testUpdateResourceNotFound() {
        int resourceId = 123;
        ResourceRequestDTO dto = createResourceRequestDTO();
        when(resourceRepository.findByIdAndStatus(eq(resourceId), eq(Resource.Status.ACTIVE.value))).thenReturn(Optional.empty());
        assertThrows(BadRequestException.class, () -> service.updateResource(resourceId, dto));
        verify(resourceRepository, never()).save(any());
    }

    private ResourceRequestDTO createResourceRequestDTO() {
        ResourceRequestDTO dto = new ResourceRequestDTO();
        ResourceSkillRequestDTO skillDTO = new ResourceSkillRequestDTO(1, 5,(byte)1);
        dto.setSkills(Set.of(skillDTO));
        dto.setEmail("test@gmail.com");
        dto.setEmployeeId(1234);
        dto.setJoiningDate(new Date());
        return dto;
    }

    private Resource createResource(int id, String email, boolean isActive) {
        Resource resource = new Resource();
        resource.setId(id);
        resource.setEmail(email);
        resource.setStatus(isActive ? Resource.Status.ACTIVE.value : Resource.Status.INACTIVE.value);
        resource.setJoiningDate(new Date());
        return resource;
    }

    @Test
    void testUpdateResourceEmailSame() {
        int resourceId = 123;
        ResourceRequestDTO dto = createResourceRequestDTO();
        Department department = new Department();
        Role role = new Role();
        role.setId(1);
        role.setName("HR");
        department.setDepartmentId(1);
        Skill validSkill = new Skill();
        validSkill.setId(dto.getSkills().iterator().next().getSkillId());

        // Create a resource with the same email as in the DTO
        Resource resource = createResource(resourceId, dto.getEmail(), true);
        resource.setRole(role);
        // Ensure that the resource is not null and set the required properties
        resource.setEmployeeId(dto.getEmployeeId());
        resource.setName(dto.getName());
        resource.setExperience(dto.getExperience());
        resource.setJoiningDate(dto.getJoiningDate());
        resource.setRole(role);
        resource.setDepartment(department);
        resource.setStatus(Resource.Status.ACTIVE.value);

        when(skillRepository.findById(dto.getSkills().iterator().next().getSkillId())).thenReturn(Optional.of(validSkill));
        when(resourceRepository.findByIdAndStatus(eq(resourceId), eq(Resource.Status.ACTIVE.value))).thenReturn(Optional.of(resource));
        when(resourceRepository.findByEmailIgnoreCase(dto.getEmail())).thenReturn(Optional.of(resource)); // Same email
        when(departmentRepository.findById(dto.getDepartmentId())).thenReturn(Optional.of(department));
        when(roleRepository.findById(dto.getRole())).thenReturn(Optional.of(role));
        service.updateResource(resourceId, dto);
        verify(resourceRepository, times(1)).save(eq(resource));
    }

    @Test
    void testUpdateResourceInvalidRole() {
        Department department = new Department();
        department.setDepartmentId(1);
        ResourceRequestDTO dto = new ResourceRequestDTO();
        Resource resource = new Resource(1);
        dto.setRole(1);
        dto.setDepartmentId(1);
        dto.setEmployeeId(1);
        when(departmentRepository.findById(dto.getDepartmentId())).thenReturn(Optional.of(department));
        when(resourceRepository.findByIdAndStatus(1, (byte) 1)).thenReturn(Optional.of(resource));
        when(roleRepository.findById(dto.getRole())).thenReturn(Optional.empty());
        assertThrows(BadRequestException.class, () -> service.updateResource(1, dto));
        verify(resourceRepository, never()).save(any(Resource.class));
        verify(resourceSkillRepository, never()).save(any(ResourceSkill.class));
    }

    @Test
    void testUpdateResourceInvalidDepartment() {
        ResourceRequestDTO dto = new ResourceRequestDTO();
        dto.setDepartmentId(1);
        Resource resource = new Resource(1);
        when(resourceRepository.findByIdAndStatus(1, (byte) 1)).thenReturn(Optional.of(resource));
        when(departmentRepository.findById(dto.getDepartmentId())).thenReturn(Optional.empty());
        assertThrows(BadRequestException.class, () -> service.updateResource(1, dto));
        verify(resourceRepository, never()).save(any(Resource.class));
        verify(resourceSkillRepository, never()).save(any(ResourceSkill.class));
    }
}
