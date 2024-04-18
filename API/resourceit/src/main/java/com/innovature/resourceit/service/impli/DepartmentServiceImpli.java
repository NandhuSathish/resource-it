package com.innovature.resourceit.service.impli;

import com.innovature.resourceit.entity.Department;
import com.innovature.resourceit.entity.Resource;
import com.innovature.resourceit.entity.dto.requestdto.DepartmentRequestDTO;
import com.innovature.resourceit.exceptionhandler.BadRequestException;
import com.innovature.resourceit.repository.DepartmentRepository;
import com.innovature.resourceit.repository.ResourceRepository;
import com.innovature.resourceit.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Locale;
import java.util.Optional;

@Service
public class DepartmentServiceImpli implements DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private MessageSource messageSource;

    private static final String DEPARTMENT_NOT_FOUND = "DEPARTMENT_NOT_FOUND";

    @Override
    public void add(DepartmentRequestDTO departmentRequestDTO) {
        String departmentName = departmentRequestDTO.getName().toUpperCase();
        departmentRequestDTO.setName(departmentName);
        Optional<Department> dept = departmentRepository.findByName(departmentName);
        dept.ifPresent(value -> {
            throw new BadRequestException(messageSource.getMessage("DEPARTMENT_ALREADY_EXISTS", null, Locale.ENGLISH));
        });
        departmentRepository.findByDisplayOrder(departmentRequestDTO.getDisplayOrder()).ifPresent(value -> {
            throw new BadRequestException(messageSource.getMessage("DISPLAY_ORDER_ALREADY_EXISTS", null, Locale.ENGLISH));
        });
        departmentRepository.save(new Department(departmentRequestDTO));
    }

    @Override
    public void update(DepartmentRequestDTO departmentRequestDTO, Integer deptId) {
        Department department = departmentRepository.findById(deptId).orElseThrow(() -> new BadRequestException(messageSource.getMessage(DEPARTMENT_NOT_FOUND, null, Locale.ENGLISH)));
        String departmentName = departmentRequestDTO.getName().toUpperCase();
        departmentRequestDTO.setName(departmentName);
        Optional<Department> dept = departmentRepository.findByName(departmentName);
        if (!departmentName.equals(department.getName())) {
            dept.ifPresent(value -> {
                throw new BadRequestException(messageSource.getMessage("DEPARTMENT_ALREADY_EXISTS", null, Locale.ENGLISH));
            });
        }
        if (!departmentRequestDTO.getDisplayOrder().equals(department.getDisplayOrder())) {
            departmentRepository.findByDisplayOrder(departmentRequestDTO.getDisplayOrder()).ifPresent(value -> {
                throw new BadRequestException(messageSource.getMessage("DISPLAY_ORDER_ALREADY_EXISTS", null, Locale.ENGLISH));
            });
        }
        department.setName(departmentName);
        department.setDisplayOrder(departmentRequestDTO.getDisplayOrder());
        departmentRepository.save(department);
    }

    @Override
    public Collection<Department> fetchAll() {
        return departmentRepository.findAllByOrderByDisplayOrder();
    }

    @Override
    public void deleteDepartment(Integer deptId) {

        Optional<Department> department = departmentRepository.findById(deptId);
        if (department.isEmpty())
            throw new BadRequestException(messageSource.getMessage(DEPARTMENT_NOT_FOUND, null, Locale.ENGLISH));
        
        Optional<Resource> resource = resourceRepository.findByDepartmentDepartmentId(deptId);
        if (resource.isPresent()) {
            throw new BadRequestException(messageSource.getMessage("DEPARTMENT_CANNOT_DELETE", null, Locale.ENGLISH));
        }

        departmentRepository.deleteById(deptId);
    }

    @Override
    public Department getDepartmentById(Integer deptId) {
        return departmentRepository.findById(deptId).orElseThrow(() -> new BadRequestException(messageSource.getMessage(DEPARTMENT_NOT_FOUND, null, Locale.ENGLISH)));
    }
}
