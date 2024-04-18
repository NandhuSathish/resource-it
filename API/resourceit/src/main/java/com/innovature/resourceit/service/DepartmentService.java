package com.innovature.resourceit.service;

import com.innovature.resourceit.entity.Department;
import com.innovature.resourceit.entity.dto.requestdto.DepartmentRequestDTO;

import java.util.Collection;

public interface DepartmentService {

    void add(DepartmentRequestDTO departmentRequestDTO);

    void update(DepartmentRequestDTO departmentRequestDTO,Integer deptId);

    Collection<Department> fetchAll();

    void deleteDepartment(Integer deptId);

    Department getDepartmentById(Integer deptId);
}
