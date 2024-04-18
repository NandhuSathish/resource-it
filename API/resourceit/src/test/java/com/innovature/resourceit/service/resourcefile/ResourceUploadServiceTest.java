/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.innovature.resourceit.service.resourcefile;

import com.innovature.resourceit.entity.Department;
import com.innovature.resourceit.entity.Resource;
import com.innovature.resourceit.entity.Role;
import com.innovature.resourceit.exceptionhandler.BadRequestException;
import com.innovature.resourceit.repository.DepartmentRepository;
import com.innovature.resourceit.repository.ResourceRepository;
import com.innovature.resourceit.repository.RoleRepository;
import com.innovature.resourceit.service.impli.ResourceFileUploadServiceImpli;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.apache.poi.ss.formula.functions.Column;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author abdul.fahad
 */
@SpringBootTest
@ContextConfiguration(classes = ResourceUploadServiceTest.class)
public class ResourceUploadServiceTest {

    private MultipartFile file;

    @Mock
    private MessageSource messageSource;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private ResourceRepository resourceRepository;

    @InjectMocks
    private ResourceFileUploadServiceImpli resourceFileUploadServiceImpli;

    @Mock
    private Sheet sheet;

    @Mock
    private Workbook workbook;

    @Mock
    private Row row;

    @Mock
    private Cell cell;

    @BeforeEach
    public void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        resourceFileUploadServiceImpli = new ResourceFileUploadServiceImpli(messageSource, departmentRepository, roleRepository, resourceRepository);
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet("Sheet1");
        Row row1 = sheet.createRow(0);
        Cell cell0 = row1.createCell(0);
        cell0.setCellValue("heading");

        row = sheet.createRow(1);
        Cell cell1 = row.createCell(0);
        cell1.setCellValue(12345);
        Cell cell2 = row.createCell(1);
        cell2.setCellValue("Test");
        Cell cell3 = row.createCell(2);
        cell3.setCellValue("Software");
        Cell cell4 = row.createCell(3);
        cell4.setCellValue("Resource");
        Cell cell5 = row.createCell(4);
        cell5.setCellValue(16 / 10 / 33);
        Cell cell6 = row.createCell(5);
        cell6.setCellValue("testresourcenew@gmail.com");
        Cell cell7 = row.createCell(6);
        cell7.setCellValue(0);
        Cell cell8 = row.createCell(7);
        cell8.setCellValue(11);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        file = new MockMultipartFile("file", "test.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", outputStream.toByteArray());

//        inputStream = new ByteArrayInputStream("some excel content".getBytes());
    }

    @Test
    void validResourceExcelUpload() throws IOException, Exception {

        Workbook mockWorkbook = mock(Workbook.class);
        Sheet mockSheet = mock(Sheet.class);
        Row mockRow = mock(Row.class);

        String d = "Software";
        Department department = new Department(1, d, 1);
        String r = "Resource";
        Role role = new Role(r);
        when(departmentRepository.findByName(d)).thenReturn(Optional.of(department));
        when(roleRepository.findByName(r)).thenReturn(Optional.of(role));
        String status = resourceFileUploadServiceImpli.resourceExcelUpload(file);

        assertEquals("success", status);

    }

    @Test
    public void testRemoveBomWithBomPresent() throws Exception {

        byte[] bomBytes = {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};
        byte[] contentBytes = {1, 2, 3};
        byte[] combinedBytes = new byte[bomBytes.length + contentBytes.length];
        System.arraycopy(bomBytes, 0, combinedBytes, 0, bomBytes.length);
        System.arraycopy(contentBytes, 0, combinedBytes, bomBytes.length, contentBytes.length);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(combinedBytes);

        InputStream result = resourceFileUploadServiceImpli.removeBom(inputStream);

        byte[] resultBytes = new byte[contentBytes.length];
        result.read(resultBytes);

        assertEquals(contentBytes[0], resultBytes[0]);
        assertEquals(contentBytes[1], resultBytes[1]);
        assertEquals(contentBytes[2], resultBytes[2]);

        assertTrue(bomBytes[0] == (byte) 0xEF && bomBytes[1] == (byte) 0xBB && bomBytes[2] == (byte) 0xBF);

        int bytesRead;
        byte[] readBytes = new byte[3];
        bytesRead = result.read(readBytes);
        assertEquals(-1, bytesRead);
    }

    @Test
    public void testRemoveBomWithNoBomPresent() throws Exception {
        // Arrange
        byte[] contentBytes = {1, 2, 3};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(contentBytes);

        InputStream result = resourceFileUploadServiceImpli.removeBom(inputStream);

        // Assert
        byte[] resultBytes = new byte[contentBytes.length];
        result.read(resultBytes);

        assertEquals(contentBytes[0], resultBytes[0]);
        assertEquals(contentBytes[1], resultBytes[1]);
        assertEquals(contentBytes[2], resultBytes[2]);
    }

    @Test
    public void testSaveResources() {
        List<Resource> resources = new ArrayList<>();

        resourceFileUploadServiceImpli.saveResources(resources);

        verify(resourceRepository).saveAll(resources);
    }

    @Test
    public void testSaveResourcesWithException() {
        List<Resource> resources = new ArrayList<>();
        when(messageSource.getMessage(eq("INTERNAL_SERVER_ERROR"), eq(null), any()))
                .thenReturn("500-Internal Server Error");

        doThrow(new RuntimeException("500-Internal Server Error")).when(resourceRepository).saveAll(resources);

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            resourceFileUploadServiceImpli.saveResources(resources);
        });

        assertNotNull(exception);
        assertEquals("500-Internal Server Error", exception.getBody().getDetail());

    }

    @Test
    public void testFileMaxSizeChecking() {

        file = new MockMultipartFile("file",
                "test.xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                new byte[26214401]);

        MockMultipartFile inputFileWithinLimit = new MockMultipartFile("file",
                "test.xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                new byte[26214400]);

        assertThrows(BadRequestException.class, () -> resourceFileUploadServiceImpli.fileMaxSizeChecking(file));

        assertDoesNotThrow(() -> resourceFileUploadServiceImpli.fileMaxSizeChecking(inputFileWithinLimit));
    }

    @Test
    public void validateEmployeeId_Valid() {

        int id = 1234;

        when(resourceRepository.findByEmployeeId(id)).thenReturn(Optional.empty());

        resourceFileUploadServiceImpli.validateEmployeeId(1234);

        verify(resourceRepository, times(1)).findByEmployeeId(id);
    }

    @Test
    public void validateEmployeeId_InValid_EmployeeIdNeeded() {

        int id = 0;

        when(messageSource.getMessage(eq("EMPLOYEEID_NEEDED"), eq(null), any()))
                .thenReturn("2023-Employee id required");

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            resourceFileUploadServiceImpli.validateEmployeeId(id);
        });

        assertNotNull(exception);
        assertEquals("2023-Employee id required", exception.getBody().getDetail());

    }

    @Test
    public void validateEmployeeId_InValid_EmployeeIdLengthLessThan4() {

        int id = 123;

        when(messageSource.getMessage(eq("EMPLOYEEID_SIZE"), eq(null), any()))
                .thenReturn("2014-Employeeid length will between 4 to 8");

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            resourceFileUploadServiceImpli.validateEmployeeId(id);
        });

        assertNotNull(exception);
        assertEquals("2014-Employeeid length will between 4 to 8", exception.getBody().getDetail());

    }

    @Test
    public void validateEmployeeId_InValid_EmployeeIdDuplicate() {

        int id = 1234;

        when(resourceRepository.findByEmployeeId(id)).thenReturn(Optional.of(new Resource()));

        when(messageSource.getMessage(eq("DUPLICATE_EMPLOYEEIDS"), eq(null), any()))
                .thenReturn("2006-Duplicate employee id present");

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            resourceFileUploadServiceImpli.validateEmployeeId(id);
        });

        assertNotNull(exception);
        assertEquals("2006-Duplicate employee id present", exception.getBody().getDetail());

    }

    @Test
    public void isValidNumber_Valid() {

        when(cell.getNumericCellValue()).thenReturn(123.0);

        // Call the method under test
        int result = (int) resourceFileUploadServiceImpli.isValidNumber("employee", cell);

        // Verify the result
        assertEquals(123, result);
    }

    @Test
    public void isValidNumber_InValidInternal() {

        when(messageSource.getMessage(eq("INTERNAL_SERVER_ERROR"), eq(null), any()))
                .thenReturn("500-Internal Server Error");

        when(cell.getNumericCellValue()).thenThrow(new BadRequestException("500-Internal Server Error"));

        BadRequestException exception = assertThrows(BadRequestException.class, () -> resourceFileUploadServiceImpli.isValidNumber("default", cell));
        assertNotNull(exception);
    }

    @Test
    public void isValidNumber_InValidEmployee() {

        when(messageSource.getMessage(eq("NUMBER_FORMAT_EMPLOYEEID"), eq(null), any()))
                .thenReturn("2007-Employeeid is a number");

        when(cell.getNumericCellValue()).thenThrow(new BadRequestException("2007-Employeeid is a number"));

        BadRequestException exception = assertThrows(BadRequestException.class, () -> resourceFileUploadServiceImpli.isValidNumber("employee", cell));
        assertNotNull(exception);
        assertEquals("2007-Employeeid is a number", exception.getBody().getDetail());
    }

    @Test
    public void isValidNumber_InValidYear() {

        when(messageSource.getMessage(eq("NUMBER_FORMAT_YEAR"), eq(null), any()))
                .thenReturn("2008-Year is a number");

        when(cell.getNumericCellValue()).thenThrow(new BadRequestException("2008-Year is a number"));

        BadRequestException exception = assertThrows(BadRequestException.class, () -> resourceFileUploadServiceImpli.isValidNumber("year", cell));
        assertNotNull(exception);
        assertEquals("2008-Year is a number", exception.getBody().getDetail());
    }

    @Test
    public void isValidNumber_InValidMonth() {

        when(messageSource.getMessage(eq("NUMBER_FORMAT_MONTH"), eq(null), any()))
                .thenReturn("2024-Month is a number");

        when(cell.getNumericCellValue()).thenThrow(new BadRequestException("2024-Month is a number"));

        BadRequestException exception = assertThrows(BadRequestException.class, () -> resourceFileUploadServiceImpli.isValidNumber("month", cell));
        assertNotNull(exception);
        assertEquals("2024-Month is a number", exception.getBody().getDetail());
    }

    @Test
    public void isPositiveNumber_Valid() {

        int num = 6;

        resourceFileUploadServiceImpli.isPositiveNumber("employee", num);

        assertTrue(true);
    }

    @Test
    public void isPositiveNumber_InValidEmployeeIdLessThanZero() {

        int num = -1;

        when(messageSource.getMessage(eq("EMPID_POSITIVE_NUMBER"), eq(null), any()))
                .thenReturn("2012-Positive employee id is needed");

        when(cell.getNumericCellValue()).thenThrow(new BadRequestException("2012-Positive employee id is needed"));

        BadRequestException exception = assertThrows(BadRequestException.class, () -> resourceFileUploadServiceImpli.isPositiveNumber("employee", num));
        assertNotNull(exception);
        assertEquals("2012-Positive employee id is needed", exception.getBody().getDetail());
    }

    @Test
    public void isPositiveNumber_InValidYearLessThanZero() {

        int num = -1;

        when(messageSource.getMessage(eq("YEAR_POSITIVE_NUMBER"), eq(null), any()))
                .thenReturn("2013-Positive year count is needed");

        when(cell.getNumericCellValue()).thenThrow(new BadRequestException("2013-Positive year count is needed"));

        BadRequestException exception = assertThrows(BadRequestException.class, () -> resourceFileUploadServiceImpli.isPositiveNumber("year", num));
        assertNotNull(exception);
        assertEquals("2013-Positive year count is needed", exception.getBody().getDetail());
    }

    @Test
    public void isPositiveNumber_InValidMonthLessThanZero() {

        int num = -1;

        when(messageSource.getMessage(eq("MONTH_POSITIVE_NUMBER"), eq(null), any()))
                .thenReturn("2025-Positive month count is needed");

        when(cell.getNumericCellValue()).thenThrow(new BadRequestException("2025-Positive month count is needed"));

        BadRequestException exception = assertThrows(BadRequestException.class, () -> resourceFileUploadServiceImpli.isPositiveNumber("month", num));
        assertNotNull(exception);
        assertEquals("2025-Positive month count is needed", exception.getBody().getDetail());
    }

    @Test
    public void isPositiveNumber_InValidDefaultLessThanZero() {

        int num = -1;

        when(messageSource.getMessage(eq("INTERNAL_SERVER_ERROR"), eq(null), any()))
                .thenReturn("500-Internal Server Error");

        when(cell.getNumericCellValue()).thenThrow(new BadRequestException("500-Internal Server Error"));

        BadRequestException exception = assertThrows(BadRequestException.class, () -> resourceFileUploadServiceImpli.isPositiveNumber("non", num));
        assertNotNull(exception);
        assertEquals("500-Internal Server Error", exception.getBody().getDetail());
    }

    @Test
    public void isPositiveNumber_InValidMonthGreaterThanEleven() {
        int num = 12;

        when(messageSource.getMessage(eq("INVALID_MONTH"), eq(null), any()))
                .thenReturn("2026-month value between 0 to 11");

        when(cell.getNumericCellValue()).thenThrow(new BadRequestException("2026-month value between 0 to 11"));

        BadRequestException exception = assertThrows(BadRequestException.class, () -> resourceFileUploadServiceImpli.isPositiveNumber("month", num));
        assertNotNull(exception);
        assertEquals("2026-month value between 0 to 11", exception.getBody().getDetail());
    }

    @Test
    public void isDate_Valid() {

        Date d = new Date();

        when(cell.getDateCellValue()).thenReturn(d);

        // Call the method under test
        Date date = resourceFileUploadServiceImpli.isDate(cell);

        // Verify the result
        assertEquals(d, date);
    }

    @Test
    public void isDate_InValidFormat() {

        when(messageSource.getMessage(eq("DATE_FORMAT_MISMATCH"), eq(null), any()))
                .thenReturn("2009-Invalid date format");

        when(cell.getDateCellValue()).thenThrow(new BadRequestException("2009-Invalid date format"));

        BadRequestException exception = assertThrows(BadRequestException.class, () -> resourceFileUploadServiceImpli.isDate(cell));
        assertNotNull(exception);
        assertEquals("2009-Invalid date format", exception.getBody().getDetail());
    }

    @Test
    public void isDate_InValidDateNull() {
        Date d = null;

        when(messageSource.getMessage(eq("DATE_NULL"), eq(null), any()))
                .thenReturn("2003-Date contain null values");

        when(cell.getDateCellValue()).thenReturn(null);

        BadRequestException exception = assertThrows(BadRequestException.class, () -> resourceFileUploadServiceImpli.isDate(cell));
        assertNotNull(exception);
        assertEquals("2003-Date contain null values", exception.getBody().getDetail());
    }

    @Test
    public void isNullChecking_Valid() {
        String value = "test";
        String name = "name";
        resourceFileUploadServiceImpli.isNullChecking(name, value);

        assertTrue(true);
    }

    @Test
    public void isNullChecking_InvalidNameNull() {
        String value = "";
        String name = "name";

        when(messageSource.getMessage(eq("NAME_NEEDED"), eq(null), any()))
                .thenReturn("2020-Name required");

        BadRequestException exception = assertThrows(BadRequestException.class, () -> resourceFileUploadServiceImpli.isNullChecking(name, value));
        assertNotNull(exception);
        assertEquals("2020-Name required", exception.getBody().getDetail());
    }

    @Test
    public void isNullChecking_InvalidEmailNull() {
        String value = "";
        String name = "email";

        when(messageSource.getMessage(eq("EMAIL_NEEDED"), eq(null), any()))
                .thenReturn("2019-Email required");

        BadRequestException exception = assertThrows(BadRequestException.class, () -> resourceFileUploadServiceImpli.isNullChecking(name, value));
        assertNotNull(exception);
        assertEquals("2019-Email required", exception.getBody().getDetail());
    }

    @Test
    public void isNullChecking_InvalidRoleNull() {
        String value = "";
        String name = "role";

        when(messageSource.getMessage(eq("ROLE_NEEDED"), eq(null), any()))
                .thenReturn("2022-Role required");

        BadRequestException exception = assertThrows(BadRequestException.class, () -> resourceFileUploadServiceImpli.isNullChecking(name, value));
        assertNotNull(exception);
        assertEquals("2022-Role required", exception.getBody().getDetail());
    }

    @Test
    public void isNullChecking_InvalidDepartmentNull() {
        String value = "";
        String name = "department";

        when(messageSource.getMessage(eq("DEPARTMENT_NEEDED"), eq(null), any()))
                .thenReturn("2021-Department required");

        BadRequestException exception = assertThrows(BadRequestException.class, () -> resourceFileUploadServiceImpli.isNullChecking(name, value));
        assertNotNull(exception);
        assertEquals("2021-Department required", exception.getBody().getDetail());
    }

    @Test
    public void isNullChecking_InvalidDefaultNull() {
        String value = "";
        String name = "default";

        when(messageSource.getMessage(eq("INTERNAL_SERVER_ERROR"), eq(null), any()))
                .thenReturn("500-Internal Server Error");

        BadRequestException exception = assertThrows(BadRequestException.class, () -> resourceFileUploadServiceImpli.isNullChecking(name, value));
        assertNotNull(exception);
        assertEquals("500-Internal Server Error", exception.getBody().getDetail());
    }

    @Test
    public void isString_Valid() {

        when(cell.getStringCellValue()).thenReturn("test");

        // Call the method under test
        String result = resourceFileUploadServiceImpli.isString("name", cell);

        // Verify the result
        assertEquals("test", result);
    }

    @Test
    public void isString_InValidInternal() {

        when(messageSource.getMessage(eq("INTERNAL_SERVER_ERROR"), eq(null), any()))
                .thenReturn("500-Internal Server Error");

        when(cell.getStringCellValue()).thenThrow(new BadRequestException("500-Internal Server Error"));

        BadRequestException exception = assertThrows(BadRequestException.class, () -> resourceFileUploadServiceImpli.isString("default", cell));
        assertNotNull(exception);
    }

    @Test
    public void isString_InValidName() {

        when(messageSource.getMessage(eq("NAME_STRING"), eq(null), any()))
                .thenReturn("2017-Name should be alphanumeric");

        when(cell.getStringCellValue()).thenThrow(new BadRequestException("2017-Name should be alphanumeric"));

        BadRequestException exception = assertThrows(BadRequestException.class, () -> resourceFileUploadServiceImpli.isString("name", cell));
        assertNotNull(exception);
        assertEquals("2017-Name should be alphanumeric", exception.getBody().getDetail());
    }

    @Test
    public void isString_InValidDepartment() {

        when(messageSource.getMessage(eq("DEPARTMENT_STRING"), eq(null), any()))
                .thenReturn("2027-Department should be alphanumeric");

        when(cell.getStringCellValue()).thenThrow(new BadRequestException("2027-Department should be alphanumeric"));

        BadRequestException exception = assertThrows(BadRequestException.class, () -> resourceFileUploadServiceImpli.isString("department", cell));
        assertNotNull(exception);
        assertEquals("2027-Department should be alphanumeric", exception.getBody().getDetail());
    }

    @Test
    public void isString_InValidRole() {

        when(messageSource.getMessage(eq("ROLE_STRING"), eq(null), any()))
                .thenReturn("2028-Role should be alphanumeric");

        when(cell.getStringCellValue()).thenThrow(new BadRequestException("2028-Role should be alphanumeric"));

        BadRequestException exception = assertThrows(BadRequestException.class, () -> resourceFileUploadServiceImpli.isString("role", cell));
        assertNotNull(exception);
        assertEquals("2028-Role should be alphanumeric", exception.getBody().getDetail());
    }

    @Test
    public void isString_InValidEmail() {

        when(messageSource.getMessage(eq("EMAIL_STRING"), eq(null), any()))
                .thenReturn("2029-Email should be alphanumeric");

        when(cell.getStringCellValue()).thenThrow(new BadRequestException("2029-Email should be alphanumeric"));

        BadRequestException exception = assertThrows(BadRequestException.class, () -> resourceFileUploadServiceImpli.isString("email", cell));
        assertNotNull(exception);
        assertEquals("2029-Email should be alphanumeric", exception.getBody().getDetail());
    }

    @Test
    public void checkingName_Valid() {
        String val = "Ab Fa";

        resourceFileUploadServiceImpli.checkingName(val);

        assertTrue(true);
    }

    @Test
    public void checkingName_InValidName() {
        String val = "12343";
        when(messageSource.getMessage(eq("INVALID_NAME"), eq(null), any()))
                .thenReturn("2015-Invalid name");

        BadRequestException exception = assertThrows(BadRequestException.class, () -> resourceFileUploadServiceImpli.checkingName(val));
        assertNotNull(exception);
        assertEquals("2015-Invalid name", exception.getBody().getDetail());
    }

    @Test
    public void checkingName_InValidNameMaxLength() {
        String val = "agsgajsashakhakhakhskahskahkahkahkahkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk";
        when(messageSource.getMessage(eq("NAME_MAX_LENGTH_REACHED"), eq(null), any()))
                .thenReturn("2016-Name should be less than 50 character");

        BadRequestException exception = assertThrows(BadRequestException.class, () -> resourceFileUploadServiceImpli.checkingName(val));
        assertNotNull(exception);
        assertEquals("2016-Name should be less than 50 character", exception.getBody().getDetail());
    }

    @Test
    public void emailValidation_Valid() {
        String email = "test@gmail.com";

        resourceFileUploadServiceImpli.emailValidation(email);

        assertTrue(true);
    }

    @Test
    public void emailValidation_InValidEmail() {
        String email = "testgmail.com";
        when(messageSource.getMessage(eq("INVALID_EMAIL"), eq(null), any()))
                .thenReturn("2004-Invalid email");

        BadRequestException exception = assertThrows(BadRequestException.class, () -> resourceFileUploadServiceImpli.emailValidation(email));
        assertNotNull(exception);
        assertEquals("2004-Invalid email", exception.getBody().getDetail());
    }

    @Test
    public void emailValidation_InValidDuplicateEmail() {
        String email = "test@gmail.com";
        when(messageSource.getMessage(eq("DUPLICATE_EMAIL"), eq(null), any()))
                .thenReturn("2005-Duplicate email present");

        Resource r = new Resource();

        when(resourceRepository.findByEmail(email)).thenReturn(Optional.of(r));

        BadRequestException exception = assertThrows(BadRequestException.class, () -> resourceFileUploadServiceImpli.emailValidation(email));
        assertNotNull(exception);
        assertEquals("2005-Duplicate email present", exception.getBody().getDetail());
    }

    @Test
    public void isDepartmentPresent_Valid() {
        Department department = new Department();
        String d = "HR";

        when(departmentRepository.findByName(d)).thenReturn(Optional.of(department));

        Department depart = resourceFileUploadServiceImpli.isDepartmentPresent(d);

        assertEquals(department, depart);
    }

    @Test
    public void isDepartmentPresent_InValidNotFound() {
        String d = "HR";

        when(messageSource.getMessage(eq("DEPARTMENT_NOT_FOUND"), eq(null), any()))
                .thenReturn("1109-Department not found");

        when(departmentRepository.findByName(d)).thenReturn(Optional.empty());

        BadRequestException exception = assertThrows(BadRequestException.class, () -> resourceFileUploadServiceImpli.isDepartmentPresent(d));
        assertNotNull(exception);
        assertEquals("1109-Department not found", exception.getBody().getDetail());
    }

    @Test
    public void isRolePresent_Valid() {
        Role role = new Role();
        String r = "HR";

        when(roleRepository.findByName(r)).thenReturn(Optional.of(role));

        Role roleValue = resourceFileUploadServiceImpli.isRolePresent(r);

        assertEquals(role, roleValue);
    }

    @Test
    public void isRolePresent_InValidNotFound() {
        String r = "HR";

        when(messageSource.getMessage(eq("ROLE_NOT_FOUND"), eq(null), any()))
                .thenReturn("1105-role not found");

        when(roleRepository.findByName(r)).thenReturn(Optional.empty());

        BadRequestException exception = assertThrows(BadRequestException.class, () -> resourceFileUploadServiceImpli.isRolePresent(r));
        assertNotNull(exception);
        assertEquals("1105-role not found", exception.getBody().getDetail());
    }

    @Test
    public void emailSwitchCase_Valid() {
        List<String> emailLists = new ArrayList<>();
        emailLists.add("test123@gmail.com");
        String email = "test@gmail.com";
        when(resourceFileUploadServiceImpli.isString("email", cell)).thenReturn(email);
        String em = resourceFileUploadServiceImpli.emailSwitchCase(emailLists, cell);
        assertEquals(email, em);
    }

    @Test
    public void emailSwitchCase_InValidDuplicate() {
        List<String> emailLists = new ArrayList<>();
        emailLists.add("test@gmail.com");
        String email = "test@gmail.com";
        when(messageSource.getMessage(eq("DUPLICATE_EMAIL"), eq(null), any()))
                .thenReturn("2005-Duplicate email present");
        when(resourceFileUploadServiceImpli.isString("email", cell)).thenReturn(email);
        BadRequestException exception = assertThrows(BadRequestException.class, () -> resourceFileUploadServiceImpli.emailSwitchCase(emailLists, cell));
        assertNotNull(exception);
        assertEquals("2005-Duplicate email present", exception.getBody().getDetail());
    }

    @Test
    public void nameSwitchCase_Valid() {

        String name = "test";
        when(resourceFileUploadServiceImpli.isString("name", cell)).thenReturn(name);
        String em = resourceFileUploadServiceImpli.nameSwitchCase(cell);
        assertEquals(name, em);
    }

    @Test
    public void employeeSwitchCase_Valid() {
        List<Integer> employeeIds = new ArrayList<>();
        employeeIds.add(12345);
        int id = 12346;
//        when(cell.getNumericCellValue()).thenReturn(12346.0);
        when(resourceFileUploadServiceImpli.isValidNumber("employee", cell)).thenReturn(12346.0);
        int em = resourceFileUploadServiceImpli.employeeSwitchCase(employeeIds, cell);
        assertEquals(id, em);
    }

    @Test
    public void employeeSwitchCase_InValidDuplicate() {
        List<Integer> employeeIds = new ArrayList<>();
        employeeIds.add(12345);
        int id = 12345;
        when(messageSource.getMessage(eq("DUPLICATE_EMPLOYEEIDS"), eq(null), any()))
                .thenReturn("2006-Duplicate employee id present");
        when(resourceFileUploadServiceImpli.isValidNumber("employee", cell)).thenReturn(12345.0);
        BadRequestException exception = assertThrows(BadRequestException.class, () -> resourceFileUploadServiceImpli.employeeSwitchCase(employeeIds, cell));
        assertNotNull(exception);
        assertEquals("2006-Duplicate employee id present", exception.getBody().getDetail());
    }
}
