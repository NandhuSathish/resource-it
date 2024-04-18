package com.innovature.resourceit.repository;

import com.innovature.resourceit.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(String role);

    List<Role> findAllByIdNotOrderById(Integer id);
}
