package com.innovature.resourceit.service.skillandProficiency;

import com.innovature.resourceit.entity.*;
import com.innovature.resourceit.entity.dto.responsedto.BillabilitySummaryResponseDTO;
import com.innovature.resourceit.repository.ResourceRepository;
import com.innovature.resourceit.repository.ResourceSkillRepository;
import com.innovature.resourceit.service.Statistics.getBillingDownloadTest;
import com.innovature.resourceit.service.impli.SkillProficiencyFileUploadImpli;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@SpringBootTest
@ContextConfiguration(classes = SkillProficiencyFileUploadTest.class)
public class SkillProficiencyFileUploadTest {
    @Mock
    HttpServletResponse response;
    @Mock
    ServletOutputStream servletOutputStream;
    @Mock
    private XSSFWorkbook workbook;
    @Mock
    private XSSFSheet sheet;
    @Mock
    private ResourceRepository resourceRepository;
    @Mock
    ResourceSkillRepository resourceSkillRepository;
    @InjectMocks
    SkillProficiencyFileUploadImpli skillProficiencyFileUploadImpli;

    @BeforeEach
    public void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    void saveSkillProficiencyTest(){
        Resource resourceA = createResource(1, "test@gmail.com", true);
        Skill skill=new Skill();
        skill.setId(1);
        skill.setName("Java");
        skill.setCreatedDate(new Date());
        skill.setUpdatedDate(new Date());
        Department department=new Department();
        department.setDepartmentId(1);
        department.setName("soft");
        department.setDisplayOrder(1);
        skill.setDepartment(department);
        Byte proId=ResourceSkill.proficiencyValues.BEGINNER.value;
        ResourceSkill resourceSkill=new ResourceSkill();
        resourceSkill.setSkill(skill);
        resourceSkill.setResource(resourceA);
        resourceSkill.setProficiency(ResourceSkill.proficiencyValues.BEGINNER.value);
        resourceSkill.setId(1);
        resourceSkill.setExperience(1);
        Optional<ResourceSkill> resourceSkillOptional= Optional.of(new ResourceSkill());
        when(resourceSkillRepository.findByResourceIdAndSkillId(anyInt(),anyInt())).thenReturn(resourceSkillOptional);
        when(resourceSkillRepository.save(resourceSkill)).thenReturn(resourceSkill);
    
        skillProficiencyFileUploadImpli.saveSkillProficiency(resourceA,skill,proId,10);
    }
    @Test
    void saveSkillProficiencyTestResourceSkillNull(){
        Resource resourceA = createResource(1, "test@gmail.com", true);
        Skill skill=new Skill();
        skill.setId(1);
        skill.setName("Java");
        skill.setCreatedDate(new Date());
        skill.setUpdatedDate(new Date());
        Department department=new Department();
        department.setDepartmentId(1);
        department.setName("soft");
        department.setDisplayOrder(1);
        skill.setDepartment(department);
        Byte proId=ResourceSkill.proficiencyValues.BEGINNER.value;
        ResourceSkill resourceSkill=new ResourceSkill();

        Optional<ResourceSkill> resourceSkillOptional= Optional.empty();
        when(resourceSkillRepository.findByResourceIdAndSkillId(anyInt(),anyInt())).thenReturn(resourceSkillOptional);
        when(resourceSkillRepository.save(resourceSkill)).thenReturn(resourceSkill);

        skillProficiencyFileUploadImpli.saveSkillProficiency(resourceA,skill,proId,10);
    }
    private Resource createResource(int id, String email, boolean isActive) {
        Resource resource = new Resource();
        Department department = new Department();
        Role role = new Role(1, "Resource");
        department.setDepartmentId(1);
        resource.setId(id);
        resource.setEmail(email);
        resource.setStatus(isActive ? Resource.Status.ACTIVE.value : Resource.Status.INACTIVE.value);
        resource.setJoiningDate(Date.from(LocalDate.of(2024, 2, 26).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        resource.setDepartment(department);
        resource.setRole(role);
        resource.setAllocationStatus((byte) 0);
        resource.setExperience(36);
        resource.setEmployeeId(1);
        return resource;
    }
}
