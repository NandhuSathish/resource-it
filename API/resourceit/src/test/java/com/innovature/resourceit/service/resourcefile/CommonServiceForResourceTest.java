/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.innovature.resourceit.service.resourcefile;

import com.innovature.resourceit.entity.Department;
import com.innovature.resourceit.entity.Resource;
import com.innovature.resourceit.entity.ResourceSkill;
import com.innovature.resourceit.entity.Role;
import com.innovature.resourceit.entity.Skill;
import com.innovature.resourceit.entity.dto.responsedto.ResourceListingResponseDTO;
import com.innovature.resourceit.entity.dto.responsedto.ResourceSkillResponseDTO;
import com.innovature.resourceit.exceptionhandler.BadRequestException;
import com.innovature.resourceit.repository.AllocationRepository;
import com.innovature.resourceit.repository.ProjectRepository;
import com.innovature.resourceit.repository.ResourceRepository;
import com.innovature.resourceit.repository.ResourceSkillRepository;
import com.innovature.resourceit.repository.SkillRepository;
import com.innovature.resourceit.service.impli.CommonServiceForResourceDownloadAndListingImpli;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.test.context.ContextConfiguration;

/**
 *
 * @author abdul.fahad
 */
@SpringBootTest
@ContextConfiguration(classes = CommonServiceForResourceTest.class)
public class CommonServiceForResourceTest {

    @Mock
    private ResourceRepository resourceRepository;

    @Mock
    MessageSource messageSource;

    @Mock
    ProjectRepository projectRepository;

    @Mock
    AllocationRepository allocationRepository;

    @Mock
    ResourceSkillRepository resourceSkillRepository;

    @Mock
    SkillRepository skillRepository;

    @InjectMocks
    private CommonServiceForResourceDownloadAndListingImpli commonServiceForResourceDownloadAndListingImpli;

    private ResourceListingResponseDTO resourceListingResponseDTO;

    private Department department;

    private Role role;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getExperienceInStringFormatTest() {
        int val = 40;
        String s = commonServiceForResourceDownloadAndListingImpli.getExperienceInStringFormat(val);
        assertEquals("3.4", s);
    }

    @Test
    public void joiningDateStringFormatObjectTest() throws ParseException {
        String date = "2023-09-11 14:41:57.618";
        String s = commonServiceForResourceDownloadAndListingImpli.joiningDateStringFormat(date);
        assertEquals("11/09/2023", s);
    }

    @Test
    public void setSkillAndExperienceTest() {
        int resourceId = 1;

        Resource resource = new Resource();
        resource.setId(1);
        resource.setName("fah");

        Skill s = new Skill(1, "Java");

        ResourceSkill resourceSkill = new ResourceSkill(1, resource, s, 48,(byte) 1);

        List<ResourceSkill> resourceSkills = new ArrayList<>();
        resourceSkills.add(resourceSkill);

        when(resourceSkillRepository.findAllByResourceId(resourceId)).thenReturn(resourceSkills);
        when(skillRepository.findById(s.getId())).thenReturn(Optional.of(s));

        List<ResourceSkillResponseDTO> resourceSkillResponseDTOS = commonServiceForResourceDownloadAndListingImpli.setResourceSkillResponse(resourceId);
        String st=commonServiceForResourceDownloadAndListingImpli.skillAndExp(resourceSkillResponseDTOS);
        assertEquals("Java : 4.0Y : Intermediate", st);
    }

    @Test
    public void checkLowerExpLessThanHighExpTest() {
        int lowerExp = 12;
        int highExp = 10;

        when(messageSource.getMessage(eq("LOW_HIGH_EXPERIENCE"), eq(null), any()))
                .thenReturn("2055-Lower experience must be less than higher experience");

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            commonServiceForResourceDownloadAndListingImpli.checkLowerExpLessThanHighExp(lowerExp, highExp);
        });

        assertNotNull(exception);
        assertEquals("2055-Lower experience must be less than higher experience", exception.getBody().getDetail());
    }

    @Test
    public void checkLowerExpLessThanHighExpValidTest() {
        int lowerExp = 12;
        int highExp = 15;
        commonServiceForResourceDownloadAndListingImpli.checkLowerExpLessThanHighExp(lowerExp, highExp);
    }

    @Test
    public void setResourceSkillResponseTest() {
        int resourceId = 1;

        Skill skill = new Skill(1);
        Resource resource = new Resource(resourceId);

        ResourceSkill resourceSkill = new ResourceSkill(1, resource, skill, 43,(byte) 1);

        List<ResourceSkill> resourceSkills = Arrays.asList(resourceSkill);

        ResourceSkillResponseDTO resourceSkillResponseDTO = new ResourceSkillResponseDTO(resourceSkill);

        List<ResourceSkillResponseDTO> list = Arrays.asList(resourceSkillResponseDTO);

        when(resourceSkillRepository.findAllByResourceId(resourceId)).thenReturn(resourceSkills);

        List<ResourceSkillResponseDTO> resourceSkillResponseDTOs = commonServiceForResourceDownloadAndListingImpli.setResourceSkillResponse(resourceId);

        assertEquals(list.get(0).getSkillId(), resourceSkillResponseDTOs.get(0).getSkillId());

    }

    @Test
    public void setSkillAndExperienceBeginnerTest() {
        int resourceId = 1;

        Resource resource = new Resource();
        resource.setId(1);
        resource.setName("fah");

        Skill s = new Skill(1, "Java");

        ResourceSkill resourceSkill = new ResourceSkill(1, resource, s, 48,(byte) 1);

        List<ResourceSkill> resourceSkills = new ArrayList<>();
        resourceSkills.add(resourceSkill);

        when(resourceSkillRepository.findAllByResourceId(resourceId)).thenReturn(resourceSkills);
        when(skillRepository.findById(s.getId())).thenReturn(Optional.of(s));

//        String st = commonServiceForResourceDownloadAndListingImpli.setSkillAndExperience(resourceId);
        List<ResourceSkillResponseDTO> resourceSkillResponseDTOS = commonServiceForResourceDownloadAndListingImpli.setResourceSkillResponse(resourceId);
        String st=commonServiceForResourceDownloadAndListingImpli.skillAndExp(resourceSkillResponseDTOS);

        assertEquals("Java : 4.0Y : Intermediate", st);
    }

    @Test
    public void setSkillAndExperienceIntermetiateTest() {
        int resourceId = 1;

        Resource resource = new Resource();
        resource.setId(1);
        resource.setName("fah");

        Skill s = new Skill(1, "Java");

        ResourceSkill resourceSkill = new ResourceSkill(1, resource, s, 48,(byte) 1);

        List<ResourceSkill> resourceSkills = new ArrayList<>();
        resourceSkills.add(resourceSkill);

        when(resourceSkillRepository.findAllByResourceId(resourceId)).thenReturn(resourceSkills);
        when(skillRepository.findById(s.getId())).thenReturn(Optional.of(s));
        List<ResourceSkillResponseDTO> resourceSkillResponseDTOS = commonServiceForResourceDownloadAndListingImpli.setResourceSkillResponse(resourceId);
        String st=commonServiceForResourceDownloadAndListingImpli.skillAndExp(resourceSkillResponseDTOS);

//        String st = commonServiceForResourceDownloadAndListingImpli.setSkillAndExperience(resourceId);
        assertEquals("Java : 4.0Y : Intermediate", st);
    }

    @Test
    void testGetDateFromStringHyphen_Success() throws ParseException {

        String dateString = "2023-12-01";

        Date result = commonServiceForResourceDownloadAndListingImpli.getDateFromStringHyphen(dateString);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date expectedDate = sdf.parse(dateString);
        assertEquals(expectedDate, result);
    }

    @Test
    void testGetDateFromStringHyphen_InvalidFormat() {

        String invalidDateString = "2023/12/01";


        when(messageSource.getMessage(eq("DATE_FORMAT_ERROR"), eq(null), any()))
                .thenReturn("1336-Date format error.");

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            commonServiceForResourceDownloadAndListingImpli.getDateFromStringHyphen(invalidDateString);
        });

        assertNotNull(exception);
        assertEquals("1336-Date format error.", exception.getBody().getDetail());
    }

}
