package com.innovature.resourceit.service.resource;

import com.innovature.resourceit.entity.*;
import com.innovature.resourceit.entity.dto.requestdto.ResourceSkillRequestDTO;
import com.innovature.resourceit.entity.dto.responsedto.ResourceResponseDTO;
import com.innovature.resourceit.repository.*;
import com.innovature.resourceit.service.impli.ResourceServiceImpli;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.MessageSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@ContextConfiguration(classes = MapResourceToDTOTest.class)
class MapResourceToDTOTest {

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

    private static final String RESOURCE_NOT_FOUND = "resource.not.found";

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        service = new ResourceServiceImpli(messageSource, resourceRepository, skillRepository, resourceSkillRepository, departmentRepository, roleRepository);
    }

    @Test
    void testMapResourceToDTO() throws ParseException {
        // Create a mock Resource object
        Resource resource = new Resource();
        resource.setId(1);
        resource.setEmployeeId(1234);
        resource.setEmail("employee@example.com");
        resource.setName("John Doe");
        resource.setExperience(5);
        resource.setPrevExperience(5);
        resource.setAllocationStatus((byte) 0);
        resource.setStatus((byte) 1);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date joiningDate = dateFormat.parse("01-01-2022");
        Date createdDate = dateFormat.parse("02-02-2022");
        Date updatedDate = dateFormat.parse("03-03-2022");

        resource.setJoiningDate(joiningDate);
        resource.setCreatedDate(createdDate);
        resource.setUpdatedDate(updatedDate);

        Department department = new Department();
        department.setName("HR");
        department.setDepartmentId(1);
        resource.setDepartment(department);

        Role role = new Role();
        role.setName("Developer");
        role.setId(1);
        resource.setRole(role);

        List<ResourceSkill> resourceSkillList = new ArrayList<>();
        ResourceSkill resourceSkill1 = new ResourceSkill();
        resourceSkill1.setSkill(new Skill(1, "Java"));
        resourceSkill1.setExperience(3);

        ResourceSkill resourceSkill2 = new ResourceSkill();
        resourceSkill2.setSkill(new Skill(2, "Python"));
        resourceSkill2.setExperience(2);

        resourceSkillList.add(resourceSkill1);
        resourceSkillList.add(resourceSkill2);

        when(resourceSkillRepository.findAllByResourceId(1)).thenReturn(resourceSkillList);

        ResourceResponseDTO resourceDTO = service.mapResourceToDTO(resource);

        assertEquals(1, resourceDTO.getId());
        assertEquals(1234, resourceDTO.getEmployeeId());
        assertEquals(1, resourceDTO.getDepartmentId());
        assertEquals("employee@example.com", resourceDTO.getEmail());
        assertEquals("John Doe", resourceDTO.getName());
        assertEquals("01-01-2022", resourceDTO.getJoiningDate());
        assertEquals(5, resourceDTO.getExperience());
        assertEquals(1, resourceDTO.getRole());
        assertEquals((byte) 0, resourceDTO.getAllocationStatus());
        assertEquals("02-02-2022", resourceDTO.getCreatedDate());
        assertEquals("03-03-2022", resourceDTO.getUpdatedDate());
        assertEquals((byte) 1, resourceDTO.getStatus());

        // Verify the skills list
        List<ResourceSkillRequestDTO> resourceSkillDTOs = resourceDTO.getSkills();
        assertEquals(2, resourceSkillDTOs.size());
        assertEquals(1, resourceSkillDTOs.get(0).getSkillId());
        assertEquals(3, resourceSkillDTOs.get(0).getExperience());
        assertEquals(2, resourceSkillDTOs.get(1).getSkillId());
        assertEquals(2, resourceSkillDTOs.get(1).getExperience());
    }
}
