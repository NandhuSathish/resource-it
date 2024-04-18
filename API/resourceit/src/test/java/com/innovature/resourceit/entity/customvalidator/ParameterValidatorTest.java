package com.innovature.resourceit.entity.customvalidator;

import com.innovature.resourceit.exceptionhandler.BadRequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

@ExtendWith(MockitoExtension.class)
class ParameterValidatorTest {

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private ParameterValidator parameterValidator;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testIsNumberValid() {
        assertEquals(10, parameterValidator.isNumber("test", "10"));
    }

    @Test
    void testIsNumberProjectId() {
        assertEquals(0, parameterValidator.isNumber("projectId", null));
    }

    @Test
    void testIsNumberResourceCount() {
        assertEquals(1, parameterValidator.isNumber("resourceCount", null));
    }

    @Test
    void testIsNumberSkill() {
        assertEquals(0, parameterValidator.isNumber("skillId", null));
    }
    
    @Test
    void testIsNumberUnknownCase() {
        assertThrows(BadRequestException.class, () -> parameterValidator.isNumber("test", null));
    }

    @Test
    void testIsNumberNullValueForEmployeeId() {
        assertEquals(0, parameterValidator.isNumber("employeeId", null));
    }

    @Test
    void testIsNumberNullValueForPageNumber() {
        assertEquals(0, parameterValidator.isNumber("pageNumber", null));
    }

    @Test
    void testIsNumberNullValueForPageSize() {
        assertEquals(5, parameterValidator.isNumber("pageSize", null));
    }

    @Test
    void testIsNumberNumberFormatException() {
        assertThrows(BadRequestException.class, () -> parameterValidator.isNumber("test", "abc"));
    }

    @Test
    void testIsNumbersNumValid() {
        List<String> nums = Arrays.asList("1", "2", "3");
        List<Integer> expected = Arrays.asList(1, 2, 3);

        assertEquals(expected, parameterValidator.isNumbersNum("test", nums));
    }

    @Test
    void testIsNumbersNumNullValue() {
        assertEquals(parameterValidator.isNumbersNum("test", Collections.emptyList()), Collections.emptyList());
    }

    @Test
    void testIsNumbersNumNumberFormatException() {
        List<String> nums = Arrays.asList("1", "2", "abc");

        when(messageSource.getMessage(any(), any(), any(Locale.class))).thenReturn("Error Message");

        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> parameterValidator.isNumbersNum("test", nums));

        assertEquals("400 BAD_REQUEST \"Error Message\"", exception.getMessage());
    }

    @Test
    void testIsExperienceNumberValid() {
        assertEquals(120, parameterValidator.isExperienceNumber("test", "10"));
    }

    @Test
    void testIsExperienceNumberNullValueForLowExperience() {
        assertEquals(0, parameterValidator.isExperienceNumber("lowExperience", null));
    }

    @Test
    void testIsExperienceNumberNullValueForHighExperience() {
        assertEquals(1300, parameterValidator.isExperienceNumber("highExperience", null));
    }

    @Test
    void testIsExperienceNumberNumberFormatException() {
        assertThrows(BadRequestException.class, () -> parameterValidator.isExperienceNumber("test", "abc"));
    }

    @Test
    public void isExperienceNumberInvalidLowerExpTest() {

        String variableName = "lowExperience";
        String num = "qwe";

        when(messageSource.getMessage(eq("EXPERIENCE_INTEGER"), eq(null), any()))
                .thenReturn("2046-experience should be numeric");

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            parameterValidator.isExperienceNumber(variableName, num);
        });

        assertNotNull(exception);
        assertEquals("2046-experience should be numeric", exception.getBody().getDetail());
    }

    @Test
    public void isExperienceNumberInvalidHigherExpTest() {

        String variableName = "highExperience";
        String num = "qwe";

        when(messageSource.getMessage(eq("EXPERIENCE_INTEGER"), eq(null), any()))
                .thenReturn("2046-experience should be numeric");

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            parameterValidator.isExperienceNumber(variableName, num);
        });

        assertNotNull(exception);
        assertEquals("2046-experience should be numeric", exception.getBody().getDetail());
    }

    @Test
    public void isNumbersNumInvalidDepartmentTest() {

        String variableName = "departmentId";
        List<String> nums = Arrays.asList("asd", "fsgaf");
        when(messageSource.getMessage(eq("DEPARTMENT_INTEGER"), eq(null), any()))
                .thenReturn("2041-Department should be numeric");

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            parameterValidator.isNumbersNum(variableName, nums);
        });

        assertNotNull(exception);
        assertEquals("2041-Department should be numeric", exception.getBody().getDetail());
    }

    @Test
    public void isNumbersNumInvalidRoleTest() {

        String variableName = "roleId";
        List<String> nums = Arrays.asList("asd", "fsgaf");
        when(messageSource.getMessage(eq("ROLE_INTEGER"), eq(null), any()))
                .thenReturn("2042-Role should be numeric");

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            parameterValidator.isNumbersNum(variableName, nums);
        });

        assertNotNull(exception);
        assertEquals("2042-Role should be numeric", exception.getBody().getDetail());
    }

    @Test
    public void isNumbersNumInvalidPROJECTTest() {

        String variableName = "projectId";
        List<String> nums = Arrays.asList("asd", "fsgaf");
        when(messageSource.getMessage(eq("PROJECTIDS_INTEGER"), eq(null), any()))
                .thenReturn("2049-Project id should be numeric");

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            parameterValidator.isNumbersNum(variableName, nums);
        });

        assertNotNull(exception);
        assertEquals("2049-Project id should be numeric", exception.getBody().getDetail());
    }

    @Test
    public void isNumbersNumInvalidAllocationStatusTest() {

        String variableName = "allocationStatus";
        List<String> nums = Arrays.asList("asd", "fsgaf");
        when(messageSource.getMessage(eq("ALLOCATION_STATUS"), eq(null), any()))
                .thenReturn("2051-Allocation status should be numeric");

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            parameterValidator.isNumbersNum(variableName, nums);
        });

        assertNotNull(exception);
        assertEquals("2051-Allocation status should be numeric", exception.getBody().getDetail());
    }

    @Test
    public void isNumbersNumInvalidAllocationTypeTest() {

        String variableName = "allocationType";
        List<String> nums = Arrays.asList("asd", "fsgaf");
        when(messageSource.getMessage(eq("ALLOCATION_TYPE"), eq(null), any()))
                .thenReturn("2050-Allocation type should be numeric");

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            parameterValidator.isNumbersNum(variableName, nums);
        });

        assertNotNull(exception);
        assertEquals("2050-Allocation type should be numeric", exception.getBody().getDetail());
    }

    @Test
    public void isNumberInvalidEmployeeIdTest() {

        String variableName = "employeeId";
        String num = "qwer";
        when(messageSource.getMessage(eq("EMPLOYEEID_INTEGER"), eq(null), any()))
                .thenReturn("2043-Employee id should be numeric");

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            parameterValidator.isNumber(variableName, num);
        });

        assertNotNull(exception);
        assertEquals("2043-Employee id should be numeric", exception.getBody().getDetail());
    }

    @Test
    public void isNumberInvalidPageNumberTest() {

        String variableName = "pageNumber";
        String num = "qwer";
        when(messageSource.getMessage(eq("PAGE_NUMBER_INTEGER"), eq(null), any()))
                .thenReturn("2053-Page number should be numeric");

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            parameterValidator.isNumber(variableName, num);
        });

        assertNotNull(exception);
        assertEquals("2053-Page number should be numeric", exception.getBody().getDetail());
    }

    @Test
    public void isNumberInvalidPageSizeTest() {

        String variableName = "pageSize";
        String num = "qwer";
        when(messageSource.getMessage(eq("PAGE_SIZE_INTEGER"), eq(null), any()))
                .thenReturn("2054-Page size should be numeric");

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            parameterValidator.isNumber(variableName, num);
        });

        assertNotNull(exception);
        assertEquals("2054-Page size should be numeric", exception.getBody().getDetail());
    }

    @Test
    public void isNumberInvalidStatusTest() {

        String variableName = "status";
        String num = "qwer";
        when(messageSource.getMessage(eq("STATUS"), eq(null), any()))
                .thenReturn("2052-Status should be numeric");

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            parameterValidator.isNumber(variableName, num);
        });

        assertNotNull(exception);
        assertEquals("2052-Status should be numeric", exception.getBody().getDetail());
    }

    @Test
    public void isNumberValidStatusTest() {

        String variableName = "status";
        String num = null;

        int i = parameterValidator.isNumber(variableName, num);

        assertEquals(1, i);
    }

    @Test
    public void isNumbersNumInvalidProjectTypeTest() {

        String variableName = "projectType";
        List<String> nums = Arrays.asList("asd", "fsgaf");
        when(messageSource.getMessage(eq("PROJECT_TYPE"), eq(null), any()))
                .thenReturn("2056-Project type should be numeric");

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            parameterValidator.isNumbersNum(variableName, nums);
        });

        assertNotNull(exception);
        assertEquals("2056-Project type should be numeric", exception.getBody().getDetail());
    }

    @Test
    public void isNumbersNumInvalidProjectStateTest() {

        String variableName = "projectState";
        List<String> nums = Arrays.asList("asd", "fsgaf");
        when(messageSource.getMessage(eq("PROJECT_STATE"), eq(null), any()))
                .thenReturn("2057-Project state should be numeric");

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            parameterValidator.isNumbersNum(variableName, nums);
        });

        assertNotNull(exception);
        assertEquals("2057-Project state should be numeric", exception.getBody().getDetail());
    }

    @Test
    public void isNumbersNumInvalidManagerIdTest() {

        String variableName = "managerId";
        List<String> nums = Arrays.asList("asd", "fsgaf");
        when(messageSource.getMessage(eq("MANAGERIDS_INTEGER"), eq(null), any()))
                .thenReturn("2058-Manager id should be numeric");

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            parameterValidator.isNumbersNum(variableName, nums);
        });

        assertNotNull(exception);
        assertEquals("2058-Manager id should be numeric", exception.getBody().getDetail());
    }

    @Test
    public void isNumbersNumInvalidApprovalStatusTest() {

        String variableName = "approvalStatus";
        List<String> nums = Arrays.asList("asd", "fsgaf");
        when(messageSource.getMessage(eq("APPROVAL_STATUS"), eq(null), any()))
                .thenReturn("2065-Approval status should be numeric");

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            parameterValidator.isNumbersNum(variableName, nums);
        });

        assertNotNull(exception);
        assertEquals("2065-Approval status should be numeric", exception.getBody().getDetail());
    }

    @Test
    public void isNumberInvalidProjectIdTest() {

        String variableName = "projectId";
        String num = "qwer";
        when(messageSource.getMessage(eq("PROJECTIDS_INTEGER"), eq(null), any()))
                .thenReturn("2049-Project id should be numeric");

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            parameterValidator.isNumber(variableName, num);
        });

        assertNotNull(exception);
        assertEquals("2049-Project id should be numeric", exception.getBody().getDetail());
    }

    @Test
    public void isNumberInvalidDepartmentTest() {

        String variableName = "departmentId";
        String num = "qwer";
        when(messageSource.getMessage(eq("DEPARTMENT_INTEGER"), eq(null), any()))
                .thenReturn("2041-Department should be numeric");

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            parameterValidator.isNumber(variableName, num);
        });

        assertNotNull(exception);
        assertEquals("2041-Department should be numeric", exception.getBody().getDetail());
    }

    @Test
    public void isNumberInvalidRESOURCECOUNTTest() {

        String variableName = "resourceCount";
        String num = "qwer";
        when(messageSource.getMessage(eq("RESOURCE_COUNT"), eq(null), any()))
                .thenReturn("2063-Resource count should be numeric");

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            parameterValidator.isNumber(variableName, num);
        });

        assertNotNull(exception);
        assertEquals("2063-Resource count should be numeric", exception.getBody().getDetail());
    }

    @Test
    public void isNumberInvalidExperienceTest() {

        String variableName = "experience";
        String num = "qwer";
        when(messageSource.getMessage(eq("EXPERIENCE_INTEGER"), eq(null), any()))
                .thenReturn("2046-experience should be numeric");

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            parameterValidator.isNumber(variableName, num);
        });

        assertNotNull(exception);
        assertEquals("2046-experience should be numeric", exception.getBody().getDetail());
    }

    @Test
    public void isNumberInvalidSkillTest() {

        String variableName = "skillId";
        String num = "qwer";
        when(messageSource.getMessage(eq("Skill_INTEGER"), eq(null), any()))
                .thenReturn("2044-Skill should be numeric");

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            parameterValidator.isNumber(variableName, num);
        });

        assertNotNull(exception);
        assertEquals("2044-Skill should be numeric", exception.getBody().getDetail());
    }
}
