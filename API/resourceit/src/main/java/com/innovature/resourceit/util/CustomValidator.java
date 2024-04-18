package com.innovature.resourceit.util;

import com.innovature.resourceit.entity.Resource;
import com.innovature.resourceit.entity.dto.requestdto.ProjectRequestRequestDTO;
import com.innovature.resourceit.exceptionhandler.BadRequestException;
import com.innovature.resourceit.repository.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Locale;

@Component
public class CustomValidator {

    @Autowired
    MessageSource messageSource;
    
    @Autowired
    ResourceRepository resourceRepository;

    public void validateProjectRequestInputParameters(String description, Date startDate, Date endDate, Byte approvalStatus, Byte projectType, Integer managerId) {
        List<Byte> projectTypeValues = List.of((byte) 0, (byte) 1,(byte) 2);
        List<Byte> approvalStatusValues = List.of((byte) 0, (byte) 1, (byte) 2);

        validateType(description, String.class, messageSource.getMessage("INVALID_DESCRIPTION", null, Locale.ENGLISH));
        validateType(startDate, Date.class, messageSource.getMessage("INVALID_START_DATE", null, Locale.ENGLISH));
        validateType(endDate, Date.class, messageSource.getMessage("INVALID_END_DATE", null, Locale.ENGLISH));
        validateValue(approvalStatus, approvalStatusValues, messageSource.getMessage("INVALID_APPROVAL_STATUS", null, Locale.ENGLISH));
        validateValue(projectType, projectTypeValues, messageSource.getMessage("INVALID_PROJECT_TYPE", null, Locale.ENGLISH));
        validateType(managerId, Integer.class, messageSource.getMessage("INVALID_MANAGER_ID", null, Locale.ENGLISH));
        if (managerId != null && !resourceRepository.findByIdAndStatus(managerId, Resource.Status.ACTIVE.value).isPresent()) {
            throw new BadRequestException(messageSource.getMessage("USER_NOT_FOUND", null, Locale.ENGLISH));

        }
    }

    public void validateProjectRequestDTO(ProjectRequestRequestDTO dto) {
        List<Byte> projectTypeValues = List.of((byte) 0, (byte) 1,(byte) 2);

        validateType(dto.getDescription(), String.class, messageSource.getMessage("INVALID_DESCRIPTION", null, Locale.ENGLISH));
        validateType1(dto.getStartDate(), Date.class, messageSource.getMessage("INVALID_START_DATE", null, Locale.ENGLISH),messageSource.getMessage("START_DATE_CANNOT_BE_NULL", null, Locale.ENGLISH));
        validateType1(dto.getEndDate(), Date.class, messageSource.getMessage("INVALID_END_DATE", null, Locale.ENGLISH),messageSource.getMessage("END_DATE_CANNOT_BE_NULL", null, Locale.ENGLISH));
        if (dto.getStartDate().after(dto.getEndDate())) {
            throw new BadRequestException(messageSource.getMessage("INVALID_START_END_DATE", null, Locale.ENGLISH));

        }
        validateValue(dto.getProjectType(), projectTypeValues, messageSource.getMessage("INVALID_PROJECT_TYPE", null, Locale.ENGLISH));
        validateType1(dto.getManagerId(), Integer.class, messageSource.getMessage("INVALID_MANAGER_ID", null, Locale.ENGLISH),messageSource.getMessage("MANAGER_ID_CANNOT_BE_NULL", null, Locale.ENGLISH));
        validateType1(dto.getProjectId(), Integer.class, messageSource.getMessage("INVALID_PROJECT_ID", null, Locale.ENGLISH),messageSource.getMessage("PROJECT_ID_CANNOT_BE_NULL", null, Locale.ENGLISH));
    }

    public void validateType(Object value, Class<?> expectedType, String message) {
        if (value != null && !expectedType.isInstance(value)) {
            throw new BadRequestException(message);
        }
    }
    public void validateType1(Object value, Class<?> expectedType, String message,String nullMessage) {
        if (value == null ) {
            throw new BadRequestException(nullMessage);
        }
        if(!expectedType.isInstance(value)){
            throw new BadRequestException(message);

        }
    }

    public void validateValue(Byte value, List<Byte> validValues, String message) {
        if (value != null && !validValues.contains(value)) {
            throw new BadRequestException(message);
        }
    }


}
