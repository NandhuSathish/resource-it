package com.innovature.resourceit.controller;


import com.innovature.resourceit.entity.Department;
import com.innovature.resourceit.entity.dto.requestdto.DepartmentRequestDTO;
import com.innovature.resourceit.entity.dto.responsedto.SuccessResponseDTO;
import com.innovature.resourceit.exceptionhandler.BadRequestException;
import com.innovature.resourceit.service.DepartmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Locale;


@RestController
@RequestMapping("/api/v1/department")
public class DepartmentController {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService, MessageSource messageSource) {
        this.departmentService = departmentService;
        this.messageSource = messageSource;
    }

    @PostMapping
    public ResponseEntity<Object> add(@Valid @RequestBody DepartmentRequestDTO departmentRequestDTO) {
        departmentService.add(departmentRequestDTO);
        String[] list = messageSource.getMessage("DEPARTMENT_ADDED", null, Locale.ENGLISH).split("-");
        return new ResponseEntity<>(new SuccessResponseDTO(list[0], list[1]), HttpStatus.CREATED);
    }

    @PutMapping("/{deptId}")
    public ResponseEntity<Object> update(@PathVariable("deptId") Integer deptId,
                                         @Valid @RequestBody DepartmentRequestDTO departmentRequestDTO) {
        departmentService.update(departmentRequestDTO, deptId);
        String[] list = messageSource.getMessage("DEPARTMENT_UPDATED", null, Locale.ENGLISH).split("-");
        return new ResponseEntity<>(new SuccessResponseDTO(list[0], list[1]), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Object> fetchAll() {
        try {
            Collection<Department> departments = departmentService.fetchAll();
            return new ResponseEntity<>(departments, HttpStatus.OK);
        } catch (Exception e) {
            throw new BadRequestException(messageSource.getMessage("DEPARTMENT_FETCHING_FAILED", null, Locale.ENGLISH));
        }
    }

    @GetMapping("/{deptId}")
    public ResponseEntity<Object> getDepartmentById(@PathVariable("deptId") Integer deptId) {
        Department department = departmentService.getDepartmentById(deptId);
        return new ResponseEntity<>(department, HttpStatus.OK);
    }

    @DeleteMapping("/{deptId}")
    public ResponseEntity<Object> deleteDepartment(@PathVariable("deptId") Integer deptId) {
        departmentService.deleteDepartment(deptId);
        String[] list = messageSource.getMessage("DEPARTMENT_DELETED", null, Locale.ENGLISH).split("-");
        return new ResponseEntity<>(new SuccessResponseDTO(list[0], list[1]), HttpStatus.OK);
    }
}
