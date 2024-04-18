/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.innovature.resourceit.service.resourcefile;

import com.innovature.resourceit.entity.customvalidator.ParameterValidator;
import com.innovature.resourceit.entity.dto.requestdto.ResourceDownloadFilterRequestDTO;
import com.innovature.resourceit.entity.dto.requestdto.ResourceFilterSkillAndExperienceRequestDTO;
import com.innovature.resourceit.entity.dto.responsedto.ResourceListingResponseDTO;
import com.innovature.resourceit.exceptionhandler.BadRequestException;
import com.innovature.resourceit.repository.ResourceRepository;
import com.innovature.resourceit.service.impli.CommonServiceForResourceDownloadAndListingImpli;
import com.innovature.resourceit.service.impli.ResourceFileDownloadServiceImpli;
import com.innovature.resourceit.service.impli.ResourceServiceImpli;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.*;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Assertions;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.test.context.ContextConfiguration;

/**
 * @author abdul.fahad
 */
@SpringBootTest
@ContextConfiguration(classes = ResourceDownloadServiceTest.class)
public class ResourceDownloadServiceTest {

    @Mock
    private ParameterValidator parameterValidator;

    @Mock
    ResourceRepository resourceRepository;

    @Mock
    private CommonServiceForResourceDownloadAndListingImpli listingImpli;

    @Mock
    private MessageSource messageSource;
    @Mock
    private ResourceServiceImpli resourceServiceImpli;

    @Mock
    private XSSFWorkbook workbook;

    @Mock
    private XSSFSheet sheet;

    @Mock
    HttpServletResponse response;

    @InjectMocks
    private ResourceFileDownloadServiceImpli resourceFileDownloadService;

    @Mock
    ServletOutputStream servletOutputStream;

    private ResourceListingResponseDTO resourceListingResponseDTO;

    @BeforeEach
    public void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        resourceListingResponseDTO = new ResourceListingResponseDTO();

    }

    @Test
    public void resourceExcelDownloadValidWithName() throws IOException {
        ResourceDownloadFilterRequestDTO requestDTO = new ResourceDownloadFilterRequestDTO();
        requestDTO.setName("fah");
        requestDTO.setSortKey("name");
        requestDTO.setSortOrder(false);
        resourceListingResponseDTO.setId(1);
        resourceListingResponseDTO.setName("fah");
        resourceListingResponseDTO.setDepartmentName("Software");
        resourceListingResponseDTO.setProjectName("exhibitor");
        resourceListingResponseDTO.setSkillNameAndExperience("Java-(4.0Y)-2, Nodejs-(1.0Y)-1");
        resourceListingResponseDTO.setTotalExperience("5.0");
        resourceListingResponseDTO.setJoiningDate("01/03/2020");
        resourceListingResponseDTO.setEmail("fah@gmail.com");
        resourceListingResponseDTO.setAllocationStatus("Support");
        resourceListingResponseDTO.setRoleName("RESOURCE");
        resourceListingResponseDTO.setStatus("ACTIVE");
        resourceListingResponseDTO.setEmployeeId(1234);
        resourceListingResponseDTO.setAging(10);
        List<ResourceListingResponseDTO> lists = new ArrayList<>();
        lists.add(resourceListingResponseDTO);
        int employeeId = 0;
        List<Integer> departmentIds = null;
        List<Integer> roleIds = List.of(2, 3, 4);
        List<Integer> allocationTypes = null;
        List<Integer> allocationStatuses = null;
        List<Integer> projectIds = null;
        int status = 1;
        int lowExp = 0;
        int highExp = 1300;
        List<Object[]> demoDTOs = new ArrayList<>();
        demoDTOs.add(new Object[]{1, "fah", "Software", "exhibitor", 5, "01/03/2020", "fah@gmail.com", 2, "RESOURCE", 1, 1234,new Date()});
        when(resourceServiceImpli.getSortKey(requestDTO.getSortKey())).thenReturn("name");

        when(parameterValidator.isNumber("employeeId", requestDTO.getEmployeeId())).thenReturn(employeeId);
        when(parameterValidator.isNumbersNum("roleId", requestDTO.getRoleIds())).thenReturn(roleIds);
        when(parameterValidator.isNumbersNum("departmentId", requestDTO.getDepartmentIds())).thenReturn(departmentIds);
        when(parameterValidator.isNumbersNum("allocationStatus", requestDTO.getAllocationStatus())).thenReturn(allocationStatuses);
        when(parameterValidator.isNumbersNum("projectId", requestDTO.getProjectIds())).thenReturn(Collections.emptyList());
        when(parameterValidator.isExperienceNumber("lowExperience", requestDTO.getLowerExperience())).thenReturn(lowExp);
        when(parameterValidator.isExperienceNumber("highExperience", requestDTO.getHighExperience())).thenReturn(highExp);
        when(parameterValidator.isNumber("status", requestDTO.getStatus())).thenReturn(status);

        when(response.getOutputStream()).thenReturn(servletOutputStream);
        when(resourceRepository.findDownloadAllByAllFilters(requestDTO.getName(), departmentIds, employeeId, roleIds, lowExp, highExp, (byte) status,
       allocationStatuses)).thenReturn(demoDTOs);

        resourceFileDownloadService.resourceExcelDownload(response, requestDTO);
        Assertions.assertEquals(1, lists.size());
    }

    @Test
    public void resourceExcelDownloadValidWithNameAndProjectName() throws IOException {
        ResourceDownloadFilterRequestDTO requestDTO = new ResourceDownloadFilterRequestDTO();
        requestDTO.setName("fah");
        List<String> projects = Arrays.asList("1");
        requestDTO.setProjectIds(projects);

        resourceListingResponseDTO.setId(1);
        resourceListingResponseDTO.setName("fah");
        resourceListingResponseDTO.setDepartmentName("Software");
        resourceListingResponseDTO.setProjectName("exhibitor");
        resourceListingResponseDTO.setSkillNameAndExperience("Java-(4.0Y)-2, Nodejs-(1.0Y)-1");
        resourceListingResponseDTO.setTotalExperience("5.0");
        resourceListingResponseDTO.setJoiningDate("01/03/2020");
        resourceListingResponseDTO.setEmail("fah@gmail.com");
        resourceListingResponseDTO.setAllocationStatus("Support");
        resourceListingResponseDTO.setRoleName("RESOURCE");
        resourceListingResponseDTO.setStatus("ACTIVE");
        resourceListingResponseDTO.setEmployeeId(1234);

        List<ResourceListingResponseDTO> lists = new ArrayList<>();
        lists.add(resourceListingResponseDTO);
        int employeeId = 0;
        List<Integer> departmentIds = null;
        List<Integer> roleIds = List.of(2, 3, 4);
        List<Integer> allocationStatuses = null;
        List<Integer> projectIds = new ArrayList<>();
        projectIds.add(1);
        int status = 1;
        int lowExp = 0;
        int highExp = 1300;
        List<Object[]> demoDTOs = new ArrayList<>();
        demoDTOs.add(new Object[]{1, "fah", "Software", "exhibitor", 5, "01/03/2020", "fah@gmail.com", 2, "RESOURCE", 1, 1234,new Date()});
        when(resourceServiceImpli.getSortKey(requestDTO.getSortKey())).thenReturn("name");

        when(parameterValidator.isNumber("employeeId", requestDTO.getEmployeeId())).thenReturn(employeeId);
        when(parameterValidator.isNumbersNum("roleId", requestDTO.getRoleIds())).thenReturn(roleIds);
        when(parameterValidator.isNumbersNum("departmentId", requestDTO.getDepartmentIds())).thenReturn(departmentIds);
        when(parameterValidator.isNumbersNum("allocationStatus", requestDTO.getAllocationStatus())).thenReturn(allocationStatuses);
        when(parameterValidator.isNumbersNum("projectId", requestDTO.getProjectIds())).thenReturn(projectIds);
        when(parameterValidator.isExperienceNumber("lowExperience", requestDTO.getLowerExperience())).thenReturn(lowExp);
        when(parameterValidator.isExperienceNumber("highExperience", requestDTO.getHighExperience())).thenReturn(highExp);
        when(parameterValidator.isNumber("status", requestDTO.getStatus())).thenReturn(status);

        when(response.getOutputStream()).thenReturn(servletOutputStream);
        when(resourceRepository.findDownloadAllByAllFiltersWithProjectName(requestDTO.getName(), departmentIds, employeeId, roleIds, lowExp, highExp, (byte) status
               , allocationStatuses, projectIds)).thenReturn(demoDTOs);

        resourceFileDownloadService.resourceExcelDownload(response, requestDTO);
        Assertions.assertEquals(1, lists.size());

    }

    @Test
    public void resourceExcelDownloadValidWithNameAndProjectNameAndSkillExperience() throws IOException {
        ResourceDownloadFilterRequestDTO requestDTO = new ResourceDownloadFilterRequestDTO();
        requestDTO.setName("fah");
        List<String> projects = Arrays.asList("1");
        requestDTO.setProjectIds(projects);
        ResourceFilterSkillAndExperienceRequestDTO skillAndExperienceRequestDTO = new ResourceFilterSkillAndExperienceRequestDTO();
        skillAndExperienceRequestDTO.setSkillId("1");
        skillAndExperienceRequestDTO.setSkillMaxValue("2");
        skillAndExperienceRequestDTO.setSkillMinValue("3");
        List<ResourceFilterSkillAndExperienceRequestDTO> skillAndExperienceList = new ArrayList<>();
        skillAndExperienceList.add(skillAndExperienceRequestDTO);

        requestDTO.setSkillAndExperiences(skillAndExperienceList);
        requestDTO.setSortKey("name");
        requestDTO.setSortOrder(false);
        resourceListingResponseDTO.setId(1);
        resourceListingResponseDTO.setName("fah");
        resourceListingResponseDTO.setDepartmentName("Software");
        resourceListingResponseDTO.setProjectName("exhibitor");
        resourceListingResponseDTO.setSkillNameAndExperience("Java-(4.0Y)-2, Nodejs-(1.0Y)-1");
        resourceListingResponseDTO.setTotalExperience("5.0");
        resourceListingResponseDTO.setJoiningDate("01/03/2020");
        resourceListingResponseDTO.setEmail("fah@gmail.com");
        resourceListingResponseDTO.setAllocationStatus("Support");
        resourceListingResponseDTO.setRoleName("RESOURCE");
        resourceListingResponseDTO.setStatus("ACTIVE");
        resourceListingResponseDTO.setEmployeeId(1234);

        List<ResourceListingResponseDTO> lists = new ArrayList<>();
        lists.add(resourceListingResponseDTO);
        int employeeId = 0;
        List<Integer> departmentIds = null;
        List<Integer> roleIds = List.of(2, 3, 4);
        List<Integer> allocationTypes = null;
        List<Integer> allocationStatuses = null;
        List<Integer> projectIds = new ArrayList<>();
        projectIds.add(1);
        int status = 1;
        int lowExp = 0;
        int highExp = 1300;
        List<Object[]> demoDTOs = new ArrayList<>();
        demoDTOs.add(new Object[]{1, "fah", "Software", "exhibitor", 5, "01/03/2020", "fah@gmail.com", 2, "RESOURCE", 1, 1234,new Date()});


        when(resourceServiceImpli.getSortKey(requestDTO.getSortKey())).thenReturn("name");

        when(parameterValidator.isNumber("employeeId", requestDTO.getEmployeeId())).thenReturn(employeeId);
        when(parameterValidator.isNumbersNum("roleId", requestDTO.getRoleIds())).thenReturn(roleIds);
        when(parameterValidator.isNumbersNum("departmentId", requestDTO.getDepartmentIds())).thenReturn(departmentIds);
        when(parameterValidator.isNumbersNum("allocationStatus", requestDTO.getAllocationStatus())).thenReturn(allocationStatuses);
        when(parameterValidator.isNumbersNum("projectId", requestDTO.getProjectIds())).thenReturn(projectIds);
        when(parameterValidator.isExperienceNumber("lowExperience", requestDTO.getLowerExperience())).thenReturn(lowExp);
        when(parameterValidator.isExperienceNumber("highExperience", requestDTO.getHighExperience())).thenReturn(highExp);
        when(parameterValidator.isNumber("status", requestDTO.getStatus())).thenReturn(status);
//        when(listingImpli.splitSkillAndExperiences(requestDTO.getSkillAndExperiences())).thenReturn(map);

        when(response.getOutputStream()).thenReturn(servletOutputStream);
//        when(resourceRepository.findDownloadAllByAllFiltersWithProjectNameSkillsAndExperience(requestDTO.getName(), departmentIds, employeeId, roleIds, lowExp, highExp, (byte) status,
//              allocationStatuses, projectIds)).thenReturn(demoDTOs);

        resourceFileDownloadService.resourceExcelDownload(response, requestDTO);
        Assertions.assertEquals(1, lists.size());

    }

//    @Test
//    public void resourceExcelDownloadValidWithNameAndSkillExperience() throws IOException {
//        ResourceDownloadFilterRequestDTO requestDTO = new ResourceDownloadFilterRequestDTO();
//        requestDTO.setName("fah");
//        List<String> skillsAndExperience = Arrays.asList("2-1");
//        ResourceFilterSkillAndExperienceRequestDTO skillAndExperienceRequestDTO = new ResourceFilterSkillAndExperienceRequestDTO();
//        skillAndExperienceRequestDTO.setSkillId("1");
//        skillAndExperienceRequestDTO.setSkillMaxValue("2");
//        skillAndExperienceRequestDTO.setSkillMinValue("3");
//        List<ResourceFilterSkillAndExperienceRequestDTO> skillAndExperienceList = new ArrayList<>();
//        skillAndExperienceList.add(skillAndExperienceRequestDTO);
//
//        requestDTO.setSkillAndExperiences(skillAndExperienceList);
//        requestDTO.setProjectIds(Collections.emptyList());
//        requestDTO.setSortKey("name");
//        requestDTO.setSortOrder(false);
//
//        resourceListingResponseDTO.setId(1);
//        resourceListingResponseDTO.setName("fah");
//        resourceListingResponseDTO.setDepartmentName("Software");
//        resourceListingResponseDTO.setProjectName("exhibitor");
//        resourceListingResponseDTO.setSkillNameAndExperience("Java-(4.0Y)-2, Nodejs-(1.0Y)-1");
//        resourceListingResponseDTO.setTotalExperience("5.0");
//        resourceListingResponseDTO.setJoiningDate("01/03/2020");
//        resourceListingResponseDTO.setEmail("fah@gmail.com");
//        resourceListingResponseDTO.setAllocationStatus("Support");
//        resourceListingResponseDTO.setRoleName("RESOURCE");
//        resourceListingResponseDTO.setStatus("ACTIVE");
//        resourceListingResponseDTO.setEmployeeId(1234);
//        when(resourceServiceImpli.getSortKey(requestDTO.getSortKey())).thenReturn("name");
////        when(resourceFileDownloadService.getIndexForSortKey(requestDTO.getSortKey())).thenReturn(1);
//        List<ResourceListingResponseDTO> lists = new ArrayList<>();
//        lists.add(resourceListingResponseDTO);
//        int employeeId = 0;
//        List<Integer> departmentIds = null;
//        List<Integer> roleIds = List.of(2, 3, 4);
//        List<Integer> allocationStatuses = null;
//        List<Integer> projectIds = null;
//        int status = 1;
//        int lowExp = 0;
//        int highExp = 1300;
//        List<Object[]> demoDTOs = new ArrayList<>();
//        demoDTOs.add(new Object[]{1, "fah", "Software", "exhibitor", 5, "01/03/2020", "fah@gmail.com", 2, "RESOURCE", 1, 1234,new Date()});
//
//        int skill1 = 1;
//        int lowExperience1 = 0;
//        int highExperience1 = 1300;
//
//        int skill2 = 0;
//        int lowExperience2 = 0;
//        int highExperience2 = 0;
//
//        int skill3 = 0;
//        int lowExperience3 = 0;
//        int highExperience3 = 0;
//
//        int skill4 = 0;
//        int lowExperience4 = 0;
//        int highExperience4 = 0;
//
//        int skill5 = 0;
//        int lowExperience5 = 0;
//        int highExperience5 = 0;
//
//        Map<String, String> map = new HashMap<>();
//        map.put("1", "1");
//
//        when(parameterValidator.isNumber("employeeId", requestDTO.getEmployeeId())).thenReturn(employeeId);
//        when(parameterValidator.isNumbersNum("roleId", requestDTO.getRoleIds())).thenReturn(roleIds);
//        when(parameterValidator.isNumbersNum("departmentId", requestDTO.getDepartmentIds())).thenReturn(departmentIds);
//        when(parameterValidator.isNumbersNum("allocationStatus", requestDTO.getAllocationStatus())).thenReturn(allocationStatuses);
//        when(parameterValidator.isNumbersNum("projectId", requestDTO.getProjectIds())).thenReturn(Collections.emptyList());
//        when(parameterValidator.isExperienceNumber("lowExperience", requestDTO.getLowerExperience())).thenReturn(lowExp);
//        when(parameterValidator.isExperienceNumber("highExperience", requestDTO.getHighExperience())).thenReturn(highExp);
//        when(parameterValidator.isNumber("status", requestDTO.getStatus())).thenReturn(status);
//        when(listingImpli.splitSkillAndExperiences(requestDTO.getSkillAndExperiences())).thenReturn(map);
//
//        when(response.getOutputStream()).thenReturn(servletOutputStream);
//        when(resourceRepository.findDownloadAllByAllFiltersWithSkillsAndExperience(requestDTO.getName(), departmentIds, employeeId, roleIds,  lowExp, highExp, (byte) status,
//              allocationStatuses, skill1, lowExperience1, highExperience1,
//                skill2, lowExperience2, highExperience2,
//                skill3, lowExperience3, highExperience3,
//                skill4, lowExperience4, highExperience4,
//                skill5, lowExperience5, highExperience5)).thenReturn(demoDTOs);
//
//        resourceFileDownloadService.resourceExcelDownload(response, requestDTO);
//        Assertions.assertEquals(1, lists.size());
//
//    }

    @Test
    public void resourceExcelDownloadValidWithNameInvalid() throws IOException {
        ResourceDownloadFilterRequestDTO requestDTO = new ResourceDownloadFilterRequestDTO();
        requestDTO.setName("fah");

        resourceListingResponseDTO.setId(1);
        resourceListingResponseDTO.setName("fah");
        resourceListingResponseDTO.setDepartmentName("Software");
        resourceListingResponseDTO.setProjectName("exhibitor");
        resourceListingResponseDTO.setSkillNameAndExperience("Java-(4.0Y)-2, Nodejs-(1.0Y)-1");
        resourceListingResponseDTO.setTotalExperience("5.0");
        resourceListingResponseDTO.setJoiningDate("01/03/2020");
        resourceListingResponseDTO.setEmail("fah@gmail.com");
        resourceListingResponseDTO.setAllocationStatus("Support");
        resourceListingResponseDTO.setRoleName("RESOURCE");
        resourceListingResponseDTO.setStatus("ACTIVE");
        resourceListingResponseDTO.setEmployeeId(1234);

        List<ResourceListingResponseDTO> lists = new ArrayList<>();
        lists.add(resourceListingResponseDTO);
        int employeeId = 0;
        List<Integer> departmentIds = null;
        List<Integer> roleIds = List.of(2, 3, 4);
        List<Integer> allocationStatuses = null;
        List<Integer> projectIds = null;
        int status = 1;
        int lowExp = 0;
        int highExp = 1300;
        List<Object[]> demoDTOs = new ArrayList<>();
        demoDTOs.add(new Object[]{1, "fah", "Software", "exhibitor", 5, "01/03/2020", "fah@gmail.com", 2, "RESOURCE", 1, 1234,new Date()});

        when(parameterValidator.isNumber("employeeId", requestDTO.getEmployeeId())).thenReturn(employeeId);
        when(parameterValidator.isNumbersNum("roleId", requestDTO.getRoleIds())).thenReturn(roleIds);
        when(parameterValidator.isNumbersNum("departmentId", requestDTO.getDepartmentIds())).thenReturn(departmentIds);
        when(parameterValidator.isNumbersNum("allocationStatus", requestDTO.getAllocationStatus())).thenReturn(allocationStatuses);
        when(parameterValidator.isNumbersNum("projectId", requestDTO.getProjectIds())).thenReturn(Collections.emptyList());
        when(parameterValidator.isExperienceNumber("lowExperience", requestDTO.getLowerExperience())).thenReturn(lowExp);
        when(parameterValidator.isExperienceNumber("highExperience", requestDTO.getHighExperience())).thenReturn(highExp);
        when(parameterValidator.isNumber("status", requestDTO.getStatus())).thenReturn(status);

//        when(impli.fetchDataFromDb(requestDTO, employeeId, departmentId, roleId)).thenReturn(lists);
        when(response.getOutputStream()).thenReturn(servletOutputStream);
        when(resourceRepository.findDownloadAllByAllFilters(requestDTO.getName(), departmentIds, employeeId, roleIds, lowExp, highExp, (byte) status,
           allocationStatuses)).thenReturn(demoDTOs);

        doThrow(new IOException("Simulated IO Exception")).when(response).getOutputStream();

//        assertThrows(BadRequestException.class, () -> resourceFileDownloadService.resourceExcelDownload(response, requestDTO));

    }
}
