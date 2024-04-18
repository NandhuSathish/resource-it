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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.test.context.ContextConfiguration;

import java.util.Date;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@ContextConfiguration(classes = AddTest.class)
 class AddTest {
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
    public  void setUp(){
        resourceRepository = mock(ResourceRepository.class);
        messageSource = mock(MessageSource.class);
        skillRepository=mock((SkillRepository.class));
        resourceSkillRepository=mock(ResourceSkillRepository.class);
        departmentRepository=mock(DepartmentRepository.class);
        roleRepository=mock(RoleRepository.class);
        service = new ResourceServiceImpli(messageSource, resourceRepository, skillRepository, resourceSkillRepository,departmentRepository,roleRepository);
    }
    @Test
    public void testAddResourceSuccess() {
        ResourceRequestDTO dto = new ResourceRequestDTO();
        Department department = new Department();
        Skill skill=new Skill();
        skill.setId(1);
        Role role=new Role();
        role.setId(1);
        department.setDepartmentId(1);
        dto.setEmployeeId(1234);
        dto.setSkills(Set.of(new ResourceSkillRequestDTO(1, 5,(byte)1)));
        dto.setDepartmentId(1);
        dto.setRole(1);
        dto.setEmail("test@example.com");
        dto.setName("test");
        dto.setExperience(4);
        dto.setJoiningDate(new Date());
        when(departmentRepository.findById(dto.getDepartmentId())).thenReturn(Optional.of(department));
        when(skillRepository.findById(dto.getDepartmentId())).thenReturn(Optional.of(skill));
        when(roleRepository.findById(dto.getRole())).thenReturn(Optional.of(role));
        when(resourceRepository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());
        service.add(dto);
        verify(resourceRepository, times(1)).save(any(Resource.class));
        verify(resourceSkillRepository, times(dto.getSkills().size())).save(any(ResourceSkill.class));
    }

    @Test
    public void testAddResourceDuplicateEmail() {
        ResourceRequestDTO dto = new ResourceRequestDTO();
        dto.setEmail("existing@example.com");
        when(resourceRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(new Resource()));
        assertThrows(BadRequestException.class, () -> service.add(dto));
        verify(resourceRepository, never()).save(any(Resource.class));
        verify(resourceSkillRepository, never()).save(any(ResourceSkill.class));
    }

    @Test
    public void testAddResourceInvalidDepartment() {
        ResourceRequestDTO dto = new ResourceRequestDTO();
        dto.setDepartmentId(1);
        when(departmentRepository.findById(dto.getDepartmentId())).thenReturn(Optional.empty());
        assertThrows(BadRequestException.class, () -> service.add(dto));
        verify(resourceRepository, never()).save(any(Resource.class));
        verify(resourceSkillRepository, never()).save(any(ResourceSkill.class));
    }

    @Test
    public void testAddResourceInvalidSkill() {
        ResourceRequestDTO dto = new ResourceRequestDTO();
        dto.setSkills(Set.of(new ResourceSkillRequestDTO(1, 5,(byte)1)));
        for (ResourceSkillRequestDTO skill : dto.getSkills()) {
            when(skillRepository.findById(skill.getSkillId())).thenThrow(BadRequestException.class);
        }
        assertThrows(BadRequestException.class, () -> service.add(dto));
        verify(resourceRepository, never()).save(any(Resource.class));
        verify(resourceSkillRepository, never()).save(any(ResourceSkill.class));
    }
    @Test
    public void testAddResourceInvalidRole() {
        Department department=new Department();
        department.setDepartmentId(1);
        ResourceRequestDTO dto = new ResourceRequestDTO();
        dto.setRole(1);
        dto.setDepartmentId(1);
        when(departmentRepository.findById(dto.getDepartmentId())).thenReturn(Optional.of(department));
        when(roleRepository.findById(dto.getRole())).thenReturn(Optional.empty());
        assertThrows(BadRequestException.class, () -> service.add(dto));
        verify(resourceRepository, never()).save(any(Resource.class));
        verify(resourceSkillRepository, never()).save(any(ResourceSkill.class));
    }
    @Test
    public void testAddResourceEmailPresent() {
        Department department=new Department();
        department.setDepartmentId(1);
        Role role=new Role();
        role.setId(1);
        Resource resource=new Resource();
        ResourceRequestDTO dto = new ResourceRequestDTO();
        dto.setRole(1);
        dto.setDepartmentId(1);
        dto.setEmail("test@gmail.com");
        when(departmentRepository.findById(dto.getDepartmentId())).thenReturn(Optional.of(department));
        when(roleRepository.findById(dto.getRole())).thenReturn(Optional.of(role));
        when(resourceRepository.findByEmailIgnoreCase(dto.getEmail())).thenReturn(Optional.of(resource));
        assertThrows(BadRequestException.class, () -> service.add(dto));
        verify(resourceRepository, never()).save(any(Resource.class));
        verify(resourceSkillRepository, never()).save(any(ResourceSkill.class));
    }


}
