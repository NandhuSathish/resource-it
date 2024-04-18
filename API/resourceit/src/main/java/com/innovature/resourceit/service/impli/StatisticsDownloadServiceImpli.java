package com.innovature.resourceit.service.impli;

import com.innovature.resourceit.entity.Allocation;
import com.innovature.resourceit.entity.Project;
import com.innovature.resourceit.entity.Resource;
import com.innovature.resourceit.entity.dto.requestdto.BillabilitySummaryRequestDTO;
import com.innovature.resourceit.entity.dto.requestdto.ResourceAnalysisRequestDTO;
import com.innovature.resourceit.entity.dto.responsedto.BillabilitySummaryResponseDTO;
import com.innovature.resourceit.entity.dto.responsedto.PagedResponseDTO;
import com.innovature.resourceit.entity.dto.responsedto.ProjectAllocationResponseDTO;
import com.innovature.resourceit.exceptionhandler.BadRequestException;
import com.innovature.resourceit.repository.AllocationRepository;
import com.innovature.resourceit.repository.ResourceRepository;
import com.innovature.resourceit.service.StatisticsDownloadService;
import com.innovature.resourceit.service.StatisticsService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
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
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Service
public class StatisticsDownloadServiceImpli implements StatisticsDownloadService {
    private static final Logger LOGGER = LoggerFactory.getLogger(StatisticsDownloadServiceImpli.class);
    private static final String BENCH = "Bench";
    private static final String RESOURCE_DOWNLOAD_FAILED = "RESOURCE_DOWNLOAD_FAILED";
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private StatisticsService statisticsService;
    @Autowired
    private StatisticsServiceImpli statisticsServiceImpli;
    @Autowired
    private CommonServiceForResourceDownloadAndListingImpli commonServiceForResourceDownloadAndListingImpli;
    @Autowired
    private ResourceRepository resourceRepository;
    @Autowired
    private AllocationRepository allocationRepository;
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    @Override
    public void billingResourceExcelDownload(HttpServletResponse response, BillabilitySummaryRequestDTO requestDTO) {
        workbook = new XSSFWorkbook();
        writeHeader();
        requestDTO.setPageNumber("0");
        requestDTO.setPageSize("90000");
        List<BillabilitySummaryResponseDTO> list = statisticsServiceImpli.getAllResource(requestDTO).getItems();
        write(list);
        ServletOutputStream outputStream;
        try {
            outputStream = response.getOutputStream();
            workbook.write(outputStream);

            workbook.close();
            outputStream.close();
        } catch (IOException ex) {
            LOGGER.info(String.format("Error-: %s ", ex));
            throw new BadRequestException(messageSource.getMessage(RESOURCE_DOWNLOAD_FAILED, null, Locale.ENGLISH));
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
        createCellHeader(row, 4, "Project Name", style);
        createCellHeader(row, 5, "Skill", style);
        createCellHeader(row, 6, "Total Experience (Years)", style);
        createCellHeader(row, 7, "Billing Days", style);
        createCellHeader(row, 8, "Bench Days", style);
        createCellHeader(row, 9, "Joining Date", style);
    }

    private void createCellHeader(Row row, int columnCount, Object valueOfCell, CellStyle style) {
        Cell cell = row.createCell(columnCount);
        cell.setCellValue((String) valueOfCell);
        cell.setCellStyle(style);
    }

    private void write(List<BillabilitySummaryResponseDTO> billableList) {
        int rowCount = 1;
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(13);
        style.setFont(font);
        for (BillabilitySummaryResponseDTO billabilitySummaryResponseDTO : billableList) {
            Row row = sheet.createRow(rowCount++);
            createCell(row, billabilitySummaryResponseDTO, style);
        }
    }

    private void createCell(Row row, BillabilitySummaryResponseDTO valueOfCell, CellStyle style) {
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
                    cell.setCellValue(valueOfCell.getRole());
                    cell.setCellStyle(style);
                }
                case 4 -> {
                    Cell cell = row.createCell(i);
                    cell.setCellValue(valueOfCell.getProjectName());
                    cell.setCellStyle(style);
                }
                case 5 -> {
                    Cell cell = row.createCell(i);
                    cell.setCellValue(commonServiceForResourceDownloadAndListingImpli
                            .skillAndExp(valueOfCell.getResourceSkillResponseDTOs()));
                    cell.setCellStyle(style);
                }
                case 6 -> {
                    Cell cell = row.createCell(i);
                    cell.setCellValue(valueOfCell.getTotalExperience());
                    cell.setCellStyle(style);
                }

                case 7 -> {
                    Cell cell = row.createCell(i);
                    cell.setCellValue(valueOfCell.getBillableDays());
                    cell.setCellStyle(style);
                }
                case 8 -> {
                    Cell cell = row.createCell(i);
                    cell.setCellValue(valueOfCell.getBenchDays());
                    cell.setCellStyle(style);
                }

                case 9 -> {
                    Cell cell = row.createCell(i);
                    cell.setCellValue(valueOfCell.getJoiningDate());
                    cell.setCellStyle(style);
                }
                default -> throw new AssertionError();
            }
        }

    }

    public List<ProjectAllocationResponseDTO> getProjectAllocationDetails() {
        List<Resource> unsortedResourceList = resourceRepository.findAllByStatus(Resource.Status.ACTIVE.value);
        List<Resource> resourceList = new ArrayList<>(unsortedResourceList);
        resourceList.sort(Comparator.comparing(Resource::getJoiningDate));
        List<ProjectAllocationResponseDTO> responseDTOList = new ArrayList<>();
        resourceList.forEach(resource -> {
            Date startDate = resource.getJoiningDate();
            Date endDate = new Date();
            Set<Allocation> allocationSet = new HashSet<>(Stream.of(
                            allocationRepository.findAllByResourceIdAndStartDateBeforeAndEndDateAfterAndStatus(
                                    resource.getId(), endDate, startDate,
                                    Allocation.StatusValues.ACTIVE.value),
                            allocationRepository.findAllByResourceIdAndEndDateAndStatus(
                                    resource.getId(), startDate,
                                    Allocation.StatusValues.ACTIVE.value),
                            allocationRepository.findAllByResourceIdAndStartDateAndStatus(
                                    resource.getId(), endDate,
                                    Allocation.StatusValues.ACTIVE.value))
                    .flatMap(List::stream)
                    .toList());
            List<Allocation> allocationList = new ArrayList<>(allocationSet);
            allocationList.sort(Comparator.comparing(Allocation::getStartDate));
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date previousEndDate = startDate; // Initialize previousEndDate to startDate
            int count = 0;
            for (Allocation allocation : allocationList) {
                count++;
                Date allocationStartDate = allocation.getStartDate();
                Date allocationEndDate = allocation.getEndDate();
                // Check for gaps between previous allocation and current adjusted allocation
                Calendar calendar1 = Calendar.getInstance();
                calendar1.setTime(allocationStartDate);
                calendar1.add(Calendar.DAY_OF_MONTH, -1);
                Date modifiedStartDate = calendar1.getTime();
                if (previousEndDate.before(modifiedStartDate)) {
                    // If there's a gap, denote it as bench
                    ProjectAllocationResponseDTO benchDTO = new ProjectAllocationResponseDTO();
                    benchDTO.setResourceId(resource.getId());
                    benchDTO.setEmployeeId(resource.getEmployeeId());
                    benchDTO.setResourceName(resource.getName());
                    benchDTO.setProjectName(BENCH);
                    benchDTO.setProjectCode("INV_DLY_BENCH_001");
                    benchDTO.setProjectType(Project.projectTypeValues.BENCH.value);
                    // for handling overlapping of bench days
                    if (count != 1) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(previousEndDate);
                        calendar.add(Calendar.DAY_OF_MONTH, 1);
                        previousEndDate = calendar.getTime();
                    }
                    benchDTO.setStartDate(dateFormat.format(previousEndDate));
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(allocationStartDate);
                    calendar.add(Calendar.DAY_OF_MONTH, -1);
                    Date allocationStartDateModified = calendar.getTime();
                    benchDTO.setEndDate(dateFormat.format(allocationStartDateModified));
                    benchDTO.setStatus(resource.getStatus());
                    responseDTOList.add(benchDTO);
                }

                // Create DTO for the current allocation
                ProjectAllocationResponseDTO dto = new ProjectAllocationResponseDTO();
                dto.setResourceId(resource.getId());
                dto.setEmployeeId(resource.getEmployeeId());
                dto.setResourceName(resource.getName());
                dto.setProjectName(allocation.getProject().getName());
                dto.setProjectCode(allocation.getProject().getProjectCode());
                dto.setProjectType(allocation.getProject().getProjectType());
                dto.setStartDate(dateFormat.format(allocationStartDate));
                dto.setEndDate(dateFormat.format(allocationEndDate));
                dto.setStatus(resource.getStatus());
                responseDTOList.add(dto);

                previousEndDate = allocationEndDate; // Update previousEndDate
            }

            // Handle the gap after the last allocation until endDate
            if (previousEndDate.before(endDate)) {
                ProjectAllocationResponseDTO benchDTO = new ProjectAllocationResponseDTO();
                benchDTO.setResourceId(resource.getId());
                benchDTO.setEmployeeId(resource.getEmployeeId());
                benchDTO.setResourceName(resource.getName());
                benchDTO.setProjectName(BENCH);
                benchDTO.setProjectCode("INV_DLY_BENCH_001");
                benchDTO.setProjectType(Project.projectTypeValues.BENCH.value);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(previousEndDate);
                if (count != 0) {
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                }
                previousEndDate = calendar.getTime();
                benchDTO.setStartDate(dateFormat.format(previousEndDate));
                benchDTO.setEndDate(dateFormat.format(endDate));
                benchDTO.setStatus(resource.getStatus());
                responseDTOList.add(benchDTO);
            }
        });
        Collections.reverse(responseDTOList);
        return responseDTOList;
    }

    @Override
    public void getProjectAllocationDetailsExcelDownload(HttpServletResponse response, ResourceAnalysisRequestDTO requestDTO) throws ParseException {
        workbook = new XSSFWorkbook();
        writeHeaderForGetAnalysis();
        int pageSize = 90000;
        int pageNumber = 0;

        writeForProjectAllocationDetails(
                statisticsServiceImpli.getProjectAllocationDetails(requestDTO.getId(), pageNumber, pageSize, requestDTO.getStartDate(), requestDTO.getEndDate()));
        ServletOutputStream outputStream;
        try {
            outputStream = response.getOutputStream();
            workbook.write(outputStream);

            workbook.close();
            outputStream.close();
        } catch (IOException ex) {
            LOGGER.info(String.format("Error %s ", ex));
            throw new BadRequestException(messageSource.getMessage(RESOURCE_DOWNLOAD_FAILED, null, Locale.ENGLISH));
        }
    }

    private void writeHeaderForGetAnalysis() {
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
        createCellHeader(row, 2, "Project Code", style);
        createCellHeader(row, 3, "Project Name", style);
        createCellHeader(row, 4, "Project Type", style);
        createCellHeader(row, 5, "Allocation Period", style);
    }

    private void writeForProjectAllocationDetails(PagedResponseDTO<ProjectAllocationResponseDTO> lists) throws ParseException {
        int rowCount = 1;
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(13);
        style.setFont(font);
        List<ProjectAllocationResponseDTO> projectAllocationResponseDTOS = lists.getItems();
        for (ProjectAllocationResponseDTO projectAllocationResponseDTO : projectAllocationResponseDTOS) {
            Row row = sheet.createRow(rowCount++);
            createCell(row, projectAllocationResponseDTO, style);
        }
    }

    private void createCell(Row row, ProjectAllocationResponseDTO valueOfCell, CellStyle style) throws ParseException {
        for (int i = 0; i <= 5; i++) {
            sheet.autoSizeColumn(i);
            switch (i) {
                case 0 -> {
                    Cell cell = row.createCell(i);
                    cell.setCellValue(valueOfCell.getEmployeeId());
                    cell.setCellStyle(style);
                }
                case 1 -> {
                    Cell cell = row.createCell(i);
                    cell.setCellValue(valueOfCell.getResourceName());
                    cell.setCellStyle(style);
                }
                case 2 -> {
                    Cell cell = row.createCell(i);
                    cell.setCellValue(valueOfCell.getProjectCode());
                    cell.setCellStyle(style);
                }
                case 3 -> {
                    Cell cell = row.createCell(i);
                    cell.setCellValue(valueOfCell.getProjectName());
                    cell.setCellStyle(style);
                }
                case 4 -> {
                    Cell cell = row.createCell(i);
                    String projectType = "";
                    switch (valueOfCell.getProjectType()) {
                        case 1 -> projectType = "Customer Billing";
                        case 2 -> projectType = "Support";
                        case 3 -> projectType = BENCH;
                        default -> projectType = "Innovature Billing";

                    }
                    cell.setCellValue(projectType);
                    cell.setCellStyle(style);
                }
                case 5 -> {
                    Cell cell = row.createCell(i);
                    StringBuilder stringBuilder = new StringBuilder();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    stringBuilder.append(dateFormat.format(dateFormat.parse(valueOfCell.getStartDate())));
                    stringBuilder.append(" - ");
                    stringBuilder.append(dateFormat.format(dateFormat.parse(valueOfCell.getEndDate())));
                    String projectNames = !stringBuilder.isEmpty() ? stringBuilder.substring(0, stringBuilder.length()) : null;
                    cell.setCellValue(projectNames);
                    cell.setCellStyle(style);
                }
                default -> throw new AssertionError();
            }
        }
    }

    @Override
    public void getAllProjectAllocationDetailsExcelDownload(HttpServletResponse response) throws ParseException {
        workbook = new XSSFWorkbook();
        writeHeaderForGetAnalysis();
        writeForAllProjectAllocationDetails(getProjectAllocationDetails());
        ServletOutputStream outputStream;
        try {
            outputStream = response.getOutputStream();
            workbook.write(outputStream);

            workbook.close();
            outputStream.close();
        } catch (IOException ex) {
            LOGGER.info(String.format("Error %s ", ex));
            throw new BadRequestException(messageSource.getMessage(RESOURCE_DOWNLOAD_FAILED, null, Locale.ENGLISH));
        }
    }

    private void writeForAllProjectAllocationDetails(List<ProjectAllocationResponseDTO> lists) throws ParseException {
        int rowCount = 1;
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(13);
        style.setFont(font);
        for (ProjectAllocationResponseDTO projectAllocationResponseDTO : lists) {
            Row row = sheet.createRow(rowCount++);
            createCell(row, projectAllocationResponseDTO, style);
        }
    }
}
