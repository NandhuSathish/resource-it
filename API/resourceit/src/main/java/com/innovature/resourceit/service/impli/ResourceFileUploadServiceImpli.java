/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.innovature.resourceit.service.impli;

import com.innovature.resourceit.entity.Department;
import com.innovature.resourceit.entity.Resource;
import com.innovature.resourceit.entity.Role;
import com.innovature.resourceit.exceptionhandler.BadRequestException;
import com.innovature.resourceit.repository.DepartmentRepository;
import com.innovature.resourceit.repository.ResourceRepository;
import com.innovature.resourceit.repository.RoleRepository;
import com.innovature.resourceit.service.ResourceFileUploadService;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author abdul.fahad
 */
@Service
public class ResourceFileUploadServiceImpli implements ResourceFileUploadService {

    private static final String SHEET = "Sheet1";

    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceFileUploadServiceImpli.class);

    private static final String EMAIL_REGEX = "^[a-zA-Z\\d_.+-]+@[a-zA-Z\\d-]+\\.[a-zA-Z\\.]{1,16}$";

    private static final String NAME_REGEX = "^[a-zA-Z]+$";

    private List<String> headers = Arrays.asList("heading", "Employee Id", "Resource Name", "Department", "Role", "Joining Date", "Email Id", "Experience Years", "Experience Months");

    private static final String DEPARTMENT = "department";
    private static final String EMAIL = "email";
    private static final String ROLE = "role";
    private static final String NAME = "name";
    private static final String EMPLOYEE = "employee";
    private static final String YEAR = "year";
    private static final String MONTH = "month";

    private static final String INTERNAL_SERVER_ERROR = "INTERNAL_SERVER_ERROR";

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ResourceRepository resourceRepository;

    public ResourceFileUploadServiceImpli(MessageSource messageSource, DepartmentRepository departmentRepository, RoleRepository roleRepository, ResourceRepository resourceRepository) {
        this.messageSource = messageSource;
        this.departmentRepository = departmentRepository;
        this.roleRepository = roleRepository;
        this.resourceRepository = resourceRepository;
    }

    @Override
    public String resourceExcelUpload(MultipartFile file) throws IOException {

        Workbook workbook = null;
        try {
            workbook = new XSSFWorkbook(file.getInputStream());
        } catch (Exception e) {
            LOGGER.error("Incorrect file");
            throw new BadRequestException(messageSource.getMessage("INCORRECT_FILE", null, Locale.ENGLISH));
        }

        Sheet sheet = workbook.getSheet(SHEET);
        Iterator<Row> rows = sheet.iterator();

        List<Resource> resources = new ArrayList<>();
        List<String> emailLists = new ArrayList<>();
        List<Integer> employeeIds = new ArrayList<>();

        fileMaxSizeChecking(file);

        int rowNumber = 0;
        while (rows.hasNext()) {
            Row currentRow = rows.next();

            if (rowNumber == 0) {
                checkingHeaders(currentRow);
                rowNumber++;
                continue;
            }
            if (currentRow.getPhysicalNumberOfCells() != 8) {
                workbook.close();
                throw new BadRequestException(messageSource.getMessage("CONTENT_MISMATCH", null, Locale.ENGLISH));
            }

            Iterator<Cell> cellsInRow = currentRow.iterator();
            Resource resource = new Resource();
            int year = 0;
            int month = 0;
            int cellIdx = 0;
            while (cellsInRow.hasNext()) {
                Cell currentCell = cellsInRow.next();

                switch (cellIdx) {
                    case 0:
                        int id = employeeSwitchCase(employeeIds, currentCell);
                        resource.setEmployeeId(id);
                        employeeIds.add(id);
                        break;

                    case 1:
                        String name = nameSwitchCase(currentCell);
                        resource.setName(name);
                        break;

                    case 2:
                        String department = isString(DEPARTMENT, currentCell);
                        isNullChecking(DEPARTMENT, department);
                        Department depart = isDepartmentPresent(department);
                        resource.setDepartment(depart);
                        break;

                    case 3:
                        String role = isString(ROLE, currentCell);
                        isNullChecking(ROLE, role);
                        Role role1 = isRolePresent(role);
                        resource.setRole(role1);
                        break;
                    case 4:
                        Date d = isDate(currentCell);
                        resource.setJoiningDate(d);
                        resource.setLastWorkedProjectDate(d);
                        break;
                    case 5:
                        String email = emailSwitchCase(emailLists, currentCell);
                        resource.setEmail(email);
                        emailLists.add(email);
                        break;
                    case 6:
                        year = (int) isValidNumber(YEAR, currentCell);
                        isPositiveNumber(YEAR, year);
                        break;
                    case 7:
                        month = (int) isValidNumber(MONTH, currentCell);
                        isPositiveNumber(MONTH, month);
                        break;
                    default:
                        throw new BadRequestException(messageSource.getMessage(INTERNAL_SERVER_ERROR, null, Locale.ENGLISH));

                }

                cellIdx++;
            }

            int monthCountFromYear = (year == 0) ? 0 : year * 12;
            int totalExperience = month + monthCountFromYear;
            resource.setExperience(totalExperience);
            resource.setPrevExperience(totalExperience);
            resource.setCreatedDate(new Date());
            resource.setUpdatedDate(new Date());
            resource.setStatus(Resource.Status.ACTIVE.value);
            resource.setAllocationStatus(Resource.AllocationStatus.BENCH.value);
            resources.add(resource);
        }
        saveResources(resources);
        LOGGER.info("File uploaded successfully");
        workbook.close();
        return "success";
    }

    public void saveResources(List<Resource> resources) {
        try {
            resourceRepository.saveAll(resources);
        } catch (Exception e) {
            LOGGER.error(String.format("Error in save resource %s", e.getMessage()));
            throw new BadRequestException(messageSource.getMessage(INTERNAL_SERVER_ERROR, null, Locale.ENGLISH));
        }
    }

    // Switch case for spliting cells
    public int employeeSwitchCase(List<Integer> employeeIds, Cell currentCell) {
        int id = (int) isValidNumber(EMPLOYEE, currentCell);
        isPositiveNumber(EMPLOYEE, id);
        if (employeeIds.contains(id)) {
            throw new BadRequestException(messageSource.getMessage("DUPLICATE_EMPLOYEEIDS", null, Locale.ENGLISH));
        }
        validateEmployeeId(id);
        return id;
    }

    public String nameSwitchCase(Cell currentCell) {
        String name = isString(NAME, currentCell);
        isNullChecking(NAME, name);
        checkingName(name);
        return name;
    }

    public String emailSwitchCase(List<String> emailLists, Cell currentCell) {
        String email = isString(EMAIL, currentCell);
        isNullChecking(EMAIL, email);
        emailValidation(email);
        if (emailLists.contains(email)) {
            throw new BadRequestException(messageSource.getMessage("DUPLICATE_EMAIL", null, Locale.ENGLISH));
        }
        return email.toLowerCase();
    }

    // validating role is present in db
    public Role isRolePresent(String role) {
        Optional<Role> optionalRole = roleRepository.findByName(role);
        if (optionalRole.isPresent()) {
            return optionalRole.get();
        } else {
            throw new BadRequestException(messageSource.getMessage("ROLE_NOT_FOUND", null, Locale.ENGLISH));
        }
    }

    // validating department is present in db
    public Department isDepartmentPresent(String department) {
        Optional<Department> optionalDepartment = departmentRepository.findByName(department);
        if (optionalDepartment.isPresent()) {
            return optionalDepartment.get();
        } else {
            throw new BadRequestException(messageSource.getMessage("DEPARTMENT_NOT_FOUND", null, Locale.ENGLISH));
        }
    }

    // email validation
    public void emailValidation(String email) {

        if (!Pattern.matches(EMAIL_REGEX, email)) {
            throw new BadRequestException(messageSource.getMessage("INVALID_EMAIL", null, Locale.ENGLISH));
        }
        Optional<Resource> optionalResource = resourceRepository.findByEmail(email);
        if (optionalResource.isPresent()) {
            throw new BadRequestException(messageSource.getMessage("DUPLICATE_EMAIL", null, Locale.ENGLISH));
        }
    }

    // name validation
    public void checkingName(String val) {
        String[] values = val.split(" ");
        for (int i = 0; i <= values.length - 1; i++) {
            if (!Pattern.matches(NAME_REGEX, values[i])) {
                throw new BadRequestException(messageSource.getMessage("INVALID_NAME", null, Locale.ENGLISH));
            }
        }
        if (val.toCharArray().length > 50) {
            throw new BadRequestException(messageSource.getMessage("NAME_MAX_LENGTH_REACHED", null, Locale.ENGLISH));
        }
    }

    // validating content is string or not
    public String isString(String name, Cell currentCell) {
        try {
            return currentCell.getStringCellValue();
        } catch (Exception n) {
            switch (name) {
                case NAME ->
                    throw new BadRequestException(messageSource.getMessage("NAME_STRING", null, Locale.ENGLISH));
                case DEPARTMENT ->
                    throw new BadRequestException(messageSource.getMessage("DEPARTMENT_STRING", null, Locale.ENGLISH));
                case ROLE ->
                    throw new BadRequestException(messageSource.getMessage("ROLE_STRING", null, Locale.ENGLISH));
                case EMAIL ->
                    throw new BadRequestException(messageSource.getMessage("EMAIL_STRING", null, Locale.ENGLISH));
                default ->
                    throw new BadRequestException(messageSource.getMessage(INTERNAL_SERVER_ERROR, null, Locale.ENGLISH));
            }

        }
    }

    // validating content is null or not
    public void isNullChecking(String name, String value) {
        switch (name) {
            case NAME -> {
                if (value.isEmpty()) {
                    throw new BadRequestException(messageSource.getMessage("NAME_NEEDED", null, Locale.ENGLISH));
                }
            }
            case EMAIL -> {
                if (value.isEmpty()) {
                    throw new BadRequestException(messageSource.getMessage("EMAIL_NEEDED", null, Locale.ENGLISH));
                }
            }
            case ROLE -> {
                if (value.isEmpty()) {
                    throw new BadRequestException(messageSource.getMessage("ROLE_NEEDED", null, Locale.ENGLISH));
                }
            }
            case DEPARTMENT -> {
                if (value.isEmpty()) {
                    throw new BadRequestException(messageSource.getMessage("DEPARTMENT_NEEDED", null, Locale.ENGLISH));
                }
            }
            default ->
                throw new BadRequestException(messageSource.getMessage(INTERNAL_SERVER_ERROR, null, Locale.ENGLISH));
        }
    }

    // Joining date validation
    public Date isDate(Cell currentCell) {
        Date d;
        try {
            d = currentCell.getDateCellValue();
        } catch (Exception n) {
            throw new BadRequestException(messageSource.getMessage("DATE_FORMAT_MISMATCH", null, Locale.ENGLISH));
        }
        if (d == null) {
            throw new BadRequestException(messageSource.getMessage("DATE_NULL", null, Locale.ENGLISH));
        }
        if (d.after(new Date())) {
            throw new BadRequestException(messageSource.getMessage("FUTURE_DATE_NOT_ALLOWED", null, Locale.ENGLISH));
        }

        return d;
    }

    // Validating employeeId or year or month has positive number
    public void isPositiveNumber(String name, int num) {
        if (num < 0) {
            switch (name) {
                case EMPLOYEE ->
                    throw new BadRequestException(messageSource.getMessage("EMPID_POSITIVE_NUMBER", null, Locale.ENGLISH));
                case YEAR ->
                    throw new BadRequestException(messageSource.getMessage("YEAR_POSITIVE_NUMBER", null, Locale.ENGLISH));
                case MONTH ->
                    throw new BadRequestException(messageSource.getMessage("MONTH_POSITIVE_NUMBER", null, Locale.ENGLISH));
                default ->
                    throw new BadRequestException(messageSource.getMessage(INTERNAL_SERVER_ERROR, null, Locale.ENGLISH));
            }
        }

        if (num > 11 && name.equals(MONTH)) {
            throw new BadRequestException(messageSource.getMessage("INVALID_MONTH", null, Locale.ENGLISH));
        }

        if (num >= 100 && name.equals(YEAR)) {
            throw new BadRequestException(messageSource.getMessage("EXPERIENCE_ACCEPT_UPTO_TWO_DIGIT", null, Locale.ENGLISH));
        }
    }

    public double isValidNumber(String name, Cell currentCell) {
        try {
            return currentCell.getNumericCellValue();
        } catch (Exception n) {
            switch (name) {
                case EMPLOYEE ->
                    throw new BadRequestException(messageSource.getMessage("NUMBER_FORMAT_EMPLOYEEID", null, Locale.ENGLISH));
                case YEAR ->
                    throw new BadRequestException(messageSource.getMessage("NUMBER_FORMAT_YEAR", null, Locale.ENGLISH));
                case MONTH ->
                    throw new BadRequestException(messageSource.getMessage("NUMBER_FORMAT_MONTH", null, Locale.ENGLISH));
                default ->
                    throw new BadRequestException(messageSource.getMessage(INTERNAL_SERVER_ERROR, null, Locale.ENGLISH));
            }

        }
    }

    public void validateEmployeeId(int id) {
        String s = id + "";
        if (id == 0) {
            throw new BadRequestException(messageSource.getMessage("EMPLOYEEID_NEEDED", null, Locale.ENGLISH));
        }
        if (s.toCharArray().length < 4 || s.toCharArray().length > 8) {
            throw new BadRequestException(messageSource.getMessage("EMPLOYEEID_SIZE", null, Locale.ENGLISH));
        }
        Optional<Resource> optResource = resourceRepository.findByEmployeeId(id);
        if (optResource.isPresent()) {
            throw new BadRequestException(messageSource.getMessage("DUPLICATE_EMPLOYEEIDS", null, Locale.ENGLISH));
        }
    }

    public InputStream removeBom(InputStream inputStream) throws IOException {
        // Check for BOM (Byte Order Mark) and skip it if present
        PushbackInputStream pushbackInputStream = new PushbackInputStream(inputStream, 3);
        byte[] bomBytes = new byte[3];
        if (pushbackInputStream.read(bomBytes) >= 3 && bomBytes[0] == (byte) 0xEF && bomBytes[1] == (byte) 0xBB && bomBytes[2] == (byte) 0xBF) {
            // If this is a UTF-8 BOM, skip it
            return pushbackInputStream;
        }
        // No BOM found, reset and return the original input stream
        pushbackInputStream.unread(bomBytes);
        return pushbackInputStream;
    }

    public void fileMaxSizeChecking(MultipartFile inputFile) {

        long maxSize = 26214400;
        if (inputFile.getSize() > maxSize) {
            LOGGER.error("Invalid file size error. Received file size is {}", inputFile.getSize());
            throw new BadRequestException(messageSource.getMessage("INVALID_FILE_SIZE", null, Locale.ENGLISH));
        }

    }

    private void checkingHeaders(Row currentRow) {
        for (Cell currentCell : currentRow) {
            if (!headers.contains(currentCell.getStringCellValue())) {
                LOGGER.error("Invalid file headers");
                throw new BadRequestException(messageSource.getMessage("INVALID_HEADER_NAME", null, Locale.ENGLISH));
            }
        }
    }

}
