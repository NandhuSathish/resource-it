package com.innovature.resourceit.entity.dto.requestdto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Setter
@Getter
public class ResourceRequestDTO {

    @NotNull(message = "EMPLOYEEID_NEEDED")
    private Integer employeeId;

    @NotNull(message = "NAME_REQUIRED")
    @Pattern(regexp = "^[a-zA-Z]+[a-zA-Z\\s]*$", message = "NAME_ALPHANUMERIC_ONLY")
    public String name;


    @NotNull(message = "EMAIL_NEEDED")
    @Email(message = "INVALID_EMAIL_FORMAT")
    public String email;

    @NotNull(message = "EXPERIENCE_REQUIRED")
    public Integer experience;

    @NotNull(message = "JOINING_DATE_REQUIRED")
    public Date joiningDate;

    @NotNull(message = "ROLE_REQUIRED")
    public Integer role;

    @NotNull(message = "DEPARTMENT_REQUIRED")
    public Integer departmentId;

    private Set<ResourceSkillRequestDTO> skills;
}
