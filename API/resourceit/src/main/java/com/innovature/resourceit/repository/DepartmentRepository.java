package com.innovature.resourceit.repository;

import com.innovature.resourceit.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department,Integer> {

    Optional<Department> findByName(String name);

    Optional<Department> findByDisplayOrder(Integer order);

    Collection<Department> findAllByOrderByDisplayOrder();

    Department findByDepartmentId(Integer departmentId);
}
