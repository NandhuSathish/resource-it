package com.innovature.resourceit.repository;

import com.innovature.resourceit.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Integer> {

    Optional<Project> findByProjectIdAndStatusNot(Integer projectId, Byte status);

    Optional<Project> findByName(String projectName);

    List<Project> findByManagerIdAndStatusAndProjectStateNotAndEdited(Integer id, Byte status, Byte projectState, Byte edited);

    List<Project> findByStatusAndProjectStateNotAndEdited(Byte status, Byte projectState, Byte edited);

    long countByProjectTypeAndProjectStateAndStatus(Byte projectType, Byte projectState, Byte status);

    Optional<Project> findByProjectIdAndStatus(Integer projectId, Byte status);

    @Query(value = "SELECT DISTINCT(p.manager_id) FROM project p where status=:status", nativeQuery = true)
    List<Integer> findAllManagerByProject(@Param("status") byte status);

    List<Project> findAllByStatus(Byte value);

    List<Project> findAllByProjectTypeAndStatusAndProjectStateNotAndEdited(Byte projectType, Byte status, Byte projectState, Byte edited);
}
