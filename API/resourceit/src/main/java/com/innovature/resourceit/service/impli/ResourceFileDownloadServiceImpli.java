/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.innovature.resourceit.service.impli;

import com.innovature.resourceit.entity.Resource;
import com.innovature.resourceit.entity.ResourceSkill;
import com.innovature.resourceit.entity.customvalidator.ParameterValidator;
import com.innovature.resourceit.entity.dto.requestdto.ResourceDownloadFilterRequestDTO;
import com.innovature.resourceit.entity.dto.requestdto.ResourceFilterSkillAndExperienceRequestDTO;
import com.innovature.resourceit.entity.dto.responsedto.ResourceListingResponseDTO;
import com.innovature.resourceit.exceptionhandler.BadRequestException;
import com.innovature.resourceit.repository.ResourceRepository;
import com.innovature.resourceit.service.ResourceFileDownloadService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * @author
 */
@Service
public class ResourceFileDownloadServiceImpli implements ResourceFileDownloadService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceFileDownloadServiceImpli.class);

    @Autowired
    private ParameterValidator parameterValidator;
    @Autowired
    private CommonServiceForResourceDownloadAndListingImpli listingImpli;
    @Autowired
    private MessageSource messageSource;

    @Autowired
    private ResourceServiceImpli resourceServiceImpli;

    @Autowired
    private ResourceRepository resourceRepository;

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    @Override
    public void resourceExcelDownload(HttpServletResponse response, ResourceDownloadFilterRequestDTO requestDTO) {
        workbook = new XSSFWorkbook();
        writeHeader();

        write(getListForDownload(requestDTO));
        ServletOutputStream outputStream;
        try {
            outputStream = response.getOutputStream();
            workbook.write(outputStream);

            workbook.close();
            outputStream.close();
        } catch (IOException ex) {
            LOGGER.info(String.format("Error %s ", ex));
            throw new BadRequestException(messageSource.getMessage("RESOURCE_DOWNLOAD_FAILED", null, Locale.ENGLISH));
        }
    }


    private List<ResourceListingResponseDTO> getListForDownload(ResourceDownloadFilterRequestDTO requestDTO) {
        int employeeId = parameterValidator.isNumber("employeeId", requestDTO.getEmployeeId());
        List<Integer> roleIdInt = parameterValidator.isNumbersNum("roleId", requestDTO.getRoleIds());
        List<Integer> departmentIdInt = parameterValidator.isNumbersNum("departmentId", requestDTO.getDepartmentIds());
        List<Integer> allocationStatusInt = parameterValidator.isNumbersNum("allocationStatus", requestDTO.getAllocationStatus());
        List<Integer> projectIdsInt = parameterValidator.isNumbersNum("projectId", requestDTO.getProjectIds());
        int lowerExp = parameterValidator.isExperienceNumber("lowExperience", requestDTO.getLowerExperience());
        int highExp = parameterValidator.isExperienceNumber("highExperience", requestDTO.getHighExperience());
        int status = parameterValidator.isNumber("status", requestDTO.getStatus());
        List<Object[]> demoDTOs;
        listingImpli.checkLowerExpLessThanHighExp(lowerExp, highExp);

        int numSkills = 5;
        int[] skills = new int[numSkills];
        int[] lowExperiences = new int[numSkills];
        int[] highExperiences = new int[numSkills];
        List<Byte>[] proficiencyStatusArray = new List[numSkills];
        processSkillAndExperience(requestDTO.getSkillAndExperiences(), skills, lowExperiences, highExperiences, proficiencyStatusArray);


        String sortKey = resourceServiceImpli.getSortKey(requestDTO.getSortKey());
        boolean sortOrder = requestDTO.getSortOrder();


        if (roleIdInt.isEmpty()) {
            roleIdInt = Arrays.asList(Resource.Roles.HOD.getId(), Resource.Roles.RESOURCE.getId(), Resource.Roles.RM.getId(), Resource.Roles.PM.getId(), Resource.Roles.HR.getId(), Resource.Roles.DH.getId());
        } else {
            roleIdInt = new ArrayList<>(roleIdInt);
            roleIdInt.removeIf(role -> Resource.Roles.ADMIN.getId().equals(role));
        }
        for (int i = 0; i < proficiencyStatusArray.length; i++) {

            List<Byte> proficiencyStatusList = null;
            proficiencyStatusList = proficiencyStatusArray[i];

            if (proficiencyStatusList == null) {
                proficiencyStatusList = Arrays.asList(
                        ResourceSkill.proficiencyValues.BEGINNER.value,
                        ResourceSkill.proficiencyValues.INTERMEDIATE.value,
                        ResourceSkill.proficiencyValues.ADVANCED.value
                );
                proficiencyStatusArray[i] = proficiencyStatusList;
            }
        }

        if (requestDTO.getSkillAndExperiences() == null || requestDTO.getSkillAndExperiences().isEmpty()) {
            if (projectIdsInt.isEmpty()) {
                demoDTOs = resourceRepository.findDownloadAllByAllFilters(requestDTO.getName(), departmentIdInt, employeeId, roleIdInt, lowerExp, highExp, (byte) status,
                        allocationStatusInt);
            } else {
                demoDTOs = resourceRepository.findDownloadAllByAllFiltersWithProjectName(requestDTO.getName(), departmentIdInt, employeeId, roleIdInt,
                        lowerExp, highExp, (byte) status, allocationStatusInt, projectIdsInt);
            }
        } else {
            if (projectIdsInt.isEmpty()) {
                demoDTOs = resourceRepository.findDownloadAllByAllFiltersWithSkillsAndExperience(
                        requestDTO.getName(), departmentIdInt, employeeId, roleIdInt, lowerExp, highExp, (byte) status, allocationStatusInt, skills[0], lowExperiences[0], highExperiences[0], skills[1], lowExperiences[1], highExperiences[1], skills[2], lowExperiences[2], highExperiences[2], skills[3], lowExperiences[3], highExperiences[3], skills[4], lowExperiences[4], highExperiences[4], proficiencyStatusArray[0], proficiencyStatusArray[1], proficiencyStatusArray[2], proficiencyStatusArray[3], proficiencyStatusArray[4]);
            } else {

                demoDTOs = resourceRepository.findDownloadAllByAllFiltersWithProjectNameSkillsAndExperience(requestDTO.getName(), departmentIdInt, employeeId, roleIdInt,
                        lowerExp, highExp, (byte) status, allocationStatusInt, skills[0], lowExperiences[0], highExperiences[0], skills[1], lowExperiences[1], highExperiences[1], skills[2], lowExperiences[2], highExperiences[2], skills[3], lowExperiences[3], highExperiences[3], skills[4], lowExperiences[4], highExperiences[4], projectIdsInt, proficiencyStatusArray[0], proficiencyStatusArray[1], proficiencyStatusArray[2], proficiencyStatusArray[3], proficiencyStatusArray[4]);
            }
        }
        List<Object[]> sortedDemoDTOs = sortAndReturnResult(demoDTOs, sortKey, sortOrder);
        return getResourceListResponseDTO(sortedDemoDTOs);
    }

    public void processSkillAndExperience(List<ResourceFilterSkillAndExperienceRequestDTO> modifiedSkillAndExperiences, int[] skills, int[] lowExperiences, int[] highExperiences, List<Byte>[] proficiencyStatusArray) {
        if (modifiedSkillAndExperiences == null || modifiedSkillAndExperiences.isEmpty()) {
            return;
        }

        int index = 0;

        for (ResourceFilterSkillAndExperienceRequestDTO newSkillAndExperience : modifiedSkillAndExperiences) {
            String skillIdStr = newSkillAndExperience.getSkillId();

            if (isEmptyOrUndefined(skillIdStr)) {
                continue;
            }

            int skillId = Integer.parseInt(skillIdStr);
            skills[index] = skillId;

            String skillMinValueStr = newSkillAndExperience.getSkillMinValue();
            String skillMaxValueStr = newSkillAndExperience.getSkillMaxValue();
            List<Byte> proficiencyStatus = newSkillAndExperience.getProficiency();
            if (proficiencyStatus == null || proficiencyStatus.isEmpty()) {
                proficiencyStatus = Arrays.asList(
                        ResourceSkill.proficiencyValues.BEGINNER.value,
                        ResourceSkill.proficiencyValues.INTERMEDIATE.value,
                        ResourceSkill.proficiencyValues.ADVANCED.value
                );
            }
            proficiencyStatusArray[index] = proficiencyStatusArray[index] != null ? proficiencyStatusArray[index] : proficiencyStatus;

            if (isNotEmpty(skillMinValueStr) && isNotEmpty(skillMaxValueStr)) {
                int skillMinValue = Integer.parseInt(skillMinValueStr);
                int skillMaxValue = Integer.parseInt(skillMaxValueStr);
                lowExperiences[index] = skillMinValue * 12;
                highExperiences[index] = (12 * skillMaxValue) - 1;
            } else {
                lowExperiences[index] = 0;
                highExperiences[index] = 1300;
            }
            index++;
        }
    }

    private boolean isEmptyOrUndefined(String str) {
        return str == null || str.isEmpty() || str.equals("undefined");
    }


    private boolean isNotEmpty(String str) {
        return str != null && !str.isEmpty();
    }


    private List<Object[]> sortAndReturnResult(List<Object[]> demoDTOs, String sortKey, boolean sortOrder) {
        demoDTOs.sort(
                (o1, o2) -> {
                    Comparable<Object> value1 = (Comparable<Object>) o1[getIndexForSortKey(sortKey)];
                    Comparable<Object> value2 = (Comparable<Object>) o2[getIndexForSortKey(sortKey)];
                    int result = value1.compareTo(value2);
                    return sortOrder ? result : -result;
                });
        return demoDTOs;
    }

    public int getIndexForSortKey(String sortKey) {
        switch (sortKey) {
            case "employee_id" -> {
                return 10;
            }
            case "name" -> {
                return 1;
            }
            default -> {
                return 5;
            }
        }
    }

    public List<ResourceListingResponseDTO> getResourceListResponseDTO(List<Object[]> demoDTOs) {

        List<ResourceListingResponseDTO> lists = new ArrayList<>();
        for (Object[] result : demoDTOs) {
            ResourceListingResponseDTO filterRequestDTO = new ResourceListingResponseDTO();
            filterRequestDTO.setAllocationStatus(result[7] + "");
            filterRequestDTO.setProjectName(result[3] + "");
            filterRequestDTO.setDepartmentName(result[2] + "");
            filterRequestDTO.setEmail(result[6] + "");
            filterRequestDTO.setRoleName(result[8] + "");
            filterRequestDTO.setEmployeeId((int) result[10]);
            filterRequestDTO.setId((int) result[0]);
            if (result[11] == null) {
                filterRequestDTO.setAging(0);
            } else {
                LocalDate dateFromResult13 = ((Date) result[11]).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                int aging = (int) ChronoUnit.DAYS.between(dateFromResult13, LocalDate.now());
                filterRequestDTO.setAging(aging > 0 && (result[9].equals((byte) 1)) ? aging : 0);
            }
            filterRequestDTO.setResourceSkillResponseDTOs(listingImpli.setResourceSkillResponse((int) result[0]));
            filterRequestDTO.setSkillNameAndExperience(listingImpli.skillAndExp(filterRequestDTO.getResourceSkillResponseDTOs()));
            try {
                filterRequestDTO.setJoiningDate(listingImpli.joiningDateStringFormat(result[5]));
            } catch (ParseException ex) {
                throw new BadRequestException(messageSource.getMessage("DATE_FORMAT_ERROR", null, Locale.ENGLISH));
            }
            filterRequestDTO.setName(result[1] + "");
            filterRequestDTO.setTotalExperience(listingImpli.getExperienceInStringFormat((int) result[4]));
            filterRequestDTO.setStatus((result[9].equals((byte) 1)) ? Resource.Status.ACTIVE.toString() : Resource.Status.INACTIVE.toString());
            lists.add(filterRequestDTO);
        }
        return lists;
    }

    private void write(List<ResourceListingResponseDTO> lists) {
        int rowCount = 1;
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(13);
        style.setFont(font);
        for (ResourceListingResponseDTO resourceListingResponseDTO : lists) {
            Row row = sheet.createRow(rowCount++);
            createCell(row, resourceListingResponseDTO, style);
        }
    }

    private void writeHeader() {
        sheet = workbook.createSheet("Sheet1");
        Row row = sheet.createRow(0);
        row.setHeight((short) 600);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(15);
        style.setFont(font);
        createCellHeader(row, 0, "Employee Id", style);
        createCellHeader(row, 1, "Name", style);
        createCellHeader(row, 2, "Department", style);
        createCellHeader(row, 3, "Role", style);
        createCellHeader(row, 4, "Skill", style);
        createCellHeader(row, 5, "Total Experience (Years)", style);
        createCellHeader(row, 6, "Joining Date", style);
        createCellHeader(row, 7, "Email Id", style);
        createCellHeader(row, 8, "Aging (Days)", style);
        createCellHeader(row, 9, "Allocation Status", style);

    }

    private void createCellHeader(Row row, int columnCount, Object valueOfCell, CellStyle style) {
        Cell cell = row.createCell(columnCount);
        cell.setCellValue((String) valueOfCell);
        cell.setCellStyle(style);
    }

    private void createCell(Row row, ResourceListingResponseDTO valueOfCell, CellStyle style) {
        for (int i = 0; i <= 9; i++) {
            sheet.autoSizeColumn(i);
            switch (i) {
                case 0 -> {
                    Cell cell = row.createCell(i);
                    cell.setCellValue(valueOfCell.getEmployeeId());
                    cell.setCellStyle(style);
                }
                case 1 -> {
                    Cell cell = row.createCell(i);
                    cell.setCellValue(valueOfCell.getName());
                    cell.setCellStyle(style);
                }
                case 2 -> {
                    Cell cell = row.createCell(i);
                    cell.setCellValue(valueOfCell.getDepartmentName());
                    cell.setCellStyle(style);
                }
                case 3 -> {
                    Cell cell = row.createCell(i);
                    cell.setCellValue(valueOfCell.getRoleName());
                    cell.setCellStyle(style);
                }
                case 4 -> {
                    Cell cell = row.createCell(i);
                    cell.setCellValue(valueOfCell.getSkillNameAndExperience());
                    cell.setCellStyle(style);
                }
                case 5 -> {
                    Cell cell = row.createCell(i);
                    cell.setCellValue(valueOfCell.getTotalExperience());
                    cell.setCellStyle(style);
                }
                case 6 -> {
                    Cell cell = row.createCell(i);
                    cell.setCellValue(valueOfCell.getJoiningDate());
                    cell.setCellStyle(style);
                }
                case 7 -> {
                    Cell cell = row.createCell(i);
                    cell.setCellValue(valueOfCell.getEmail());
                    cell.setCellStyle(style);
                }

                case 8 -> {
                    Cell cell = row.createCell(i);
                    cell.setCellValue(valueOfCell.getAging());
                    cell.setCellStyle(style);
                }
                case 9 -> {
                    Cell cell = row.createCell(i);
                    String allocationStatus = "";
                    if (valueOfCell.getAllocationStatus().equals(String.valueOf(Resource.AllocationStatus.BENCH.value))) {
                        allocationStatus = "Bench";
                    } else if (valueOfCell.getAllocationStatus().equals(String.valueOf(Resource.AllocationStatus.INTERNAL.value))) {
                        allocationStatus = "Internal";
                    } else {
                        allocationStatus = "Billable";
                    }
                    cell.setCellValue(allocationStatus);
                    cell.setCellStyle(style);
                }


                default -> throw new AssertionError();
            }
        }

    }

}
