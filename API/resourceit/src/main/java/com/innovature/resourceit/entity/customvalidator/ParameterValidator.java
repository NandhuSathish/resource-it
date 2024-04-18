/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.innovature.resourceit.entity.customvalidator;

import com.innovature.resourceit.exceptionhandler.BadRequestException;
import java.util.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

/**
 * @author abdul.fahad
 */
@Component
public class ParameterValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParameterValidator.class);
    private static final String MUST_BE_NUMBER = " must be number";
    private static final String INTERNAL_SERVER_ERROR = "INTERNAL_SERVER_ERROR";
    private static final String PROJECT_ID = "projectId";
    private static final String DEPARTMENT_ID = "departmentId";
    private static final String EXPERIENCE = "experience";
    private static final String RESOURCE_COUNT = "resourceCount";
    private static final String EXPERIENCE_INTEGER = "EXPERIENCE_INTEGER";
    private static final String SKILLID = "skillId";

    @Autowired
    private MessageSource messageSource;

    public int isNumber(String variableName, String num) {
        try {
            if (num == null) {
                return switch (variableName) {
                    case "employeeId" ->
                        0;
                    case "pageNumber" ->
                        0;
                    case "pageSize" ->
                        5;
                    case "status" ->
                        1;
                    case PROJECT_ID, DEPARTMENT_ID, EXPERIENCE ->
                        0;
                    case RESOURCE_COUNT ->
                        1;
                    case SKILLID ->
                        0;
                    default ->
                        throw new BadRequestException(messageSource.getMessage(INTERNAL_SERVER_ERROR, null, Locale.ENGLISH));
                };
            }
            return Integer.parseInt(num);
        } catch (NumberFormatException e) {
            LOGGER.error("{}{}", variableName, MUST_BE_NUMBER);
            switch (variableName) {
                case "flag" ->
                    throw new BadRequestException(messageSource.getMessage("NUMERIC_FLAG", null, Locale.ENGLISH));
                case "allocationType" ->
                    throw new BadRequestException(messageSource.getMessage("ALLOCATION_TYPE", null, Locale.ENGLISH));
                case "employeeId" ->
                    throw new BadRequestException(messageSource.getMessage("EMPLOYEEID_INTEGER", null, Locale.ENGLISH));
                case "pageNumber" ->
                    throw new BadRequestException(messageSource.getMessage("PAGE_NUMBER_INTEGER", null, Locale.ENGLISH));
                case "pageSize" ->
                    throw new BadRequestException(messageSource.getMessage("PAGE_SIZE_INTEGER", null, Locale.ENGLISH));
                case "status" ->
                    throw new BadRequestException(messageSource.getMessage("STATUS", null, Locale.ENGLISH));
                case PROJECT_ID ->
                    throw new BadRequestException(messageSource.getMessage("PROJECTIDS_INTEGER", null, Locale.ENGLISH));
                case DEPARTMENT_ID ->
                    throw new BadRequestException(messageSource.getMessage("DEPARTMENT_INTEGER", null, Locale.ENGLISH));
                case RESOURCE_COUNT ->
                    throw new BadRequestException(messageSource.getMessage("RESOURCE_COUNT", null, Locale.ENGLISH));
                case EXPERIENCE ->
                    throw new BadRequestException(messageSource.getMessage(EXPERIENCE_INTEGER, null, Locale.ENGLISH));
                case SKILLID ->
                    throw new BadRequestException(messageSource.getMessage("Skill_INTEGER", null, Locale.ENGLISH));
                case "id" ->
                    throw new BadRequestException(messageSource.getMessage("INVALID_ID", null, Locale.ENGLISH));
                default ->
                    throw new BadRequestException(messageSource.getMessage(INTERNAL_SERVER_ERROR, null, Locale.ENGLISH));
            }

        }
    }

    public List<Integer> isNumbersNum(String variableName, List<String> nums) {
        try {
            if (nums == null||nums.isEmpty()) {
        return Collections.emptyList();
            }
            return nums.stream().map(Integer::parseInt).toList();
        } catch (Exception e) {
            LOGGER.error("{}{}", variableName, MUST_BE_NUMBER);
            switch (variableName) {
                case SKILLID ->
                    throw new BadRequestException(messageSource.getMessage("Skill_INTEGER", null, Locale.ENGLISH));
                case DEPARTMENT_ID ->
                    throw new BadRequestException(messageSource.getMessage("DEPARTMENT_INTEGER", null, Locale.ENGLISH));
                case "roleId" ->
                    throw new BadRequestException(messageSource.getMessage("ROLE_INTEGER", null, Locale.ENGLISH));
                case PROJECT_ID ->
                    throw new BadRequestException(messageSource.getMessage("PROJECTIDS_INTEGER", null, Locale.ENGLISH));
                case "allocationStatus" ->
                    throw new BadRequestException(messageSource.getMessage("ALLOCATION_STATUS", null, Locale.ENGLISH));
                case "allocationType" ->
                    throw new BadRequestException(messageSource.getMessage("ALLOCATION_TYPE", null, Locale.ENGLISH));
                case "projectType" ->
                    throw new BadRequestException(messageSource.getMessage("PROJECT_TYPE", null, Locale.ENGLISH));
                case "projectState" ->
                    throw new BadRequestException(messageSource.getMessage("PROJECT_STATE", null, Locale.ENGLISH));
                case "managerId" ->
                    throw new BadRequestException(messageSource.getMessage("MANAGERIDS_INTEGER", null, Locale.ENGLISH));
                case "approvalStatus" ->
                    throw new BadRequestException(messageSource.getMessage("APPROVAL_STATUS", null, Locale.ENGLISH));
                default ->
                    throw new BadRequestException(messageSource.getMessage(INTERNAL_SERVER_ERROR, null, Locale.ENGLISH));
            }

        }
    }

    public int isExperienceNumber(String variableName, String num) {
        try {
            if (num == null && variableName.equals("lowExperience")) {
                return 0;
            } else if (num == null && variableName.equals("highExperience")) {
                return 1300;
            }
            return Integer.parseInt(num) * 12;
        } catch (NumberFormatException e) {
            LOGGER.error("{}{}", variableName, MUST_BE_NUMBER);
            switch (variableName) {
                case "lowExperience" ->
                    throw new BadRequestException(messageSource.getMessage(EXPERIENCE_INTEGER, null, Locale.ENGLISH));
                case "highExperience" ->
                    throw new BadRequestException(messageSource.getMessage(EXPERIENCE_INTEGER, null, Locale.ENGLISH));
                default ->
                    throw new BadRequestException(messageSource.getMessage(INTERNAL_SERVER_ERROR, null, Locale.ENGLISH));
            }

        }
    }

}
