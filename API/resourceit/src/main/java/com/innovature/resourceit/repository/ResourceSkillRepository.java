/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.innovature.resourceit.repository;

import com.innovature.resourceit.entity.ResourceSkill;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author abdul.fahad
 */
@Repository
public interface ResourceSkillRepository extends JpaRepository<ResourceSkill, Integer> {

    Optional<List<ResourceSkill>> findByResourceId(int resourceId);

    Optional<ResourceSkill> findByResourceIdAndSkillId(int resourceId, int skillId);

    @Query(value = "SELECT * FROM resource_skill WHERE "
            + "(:resourceId IS NULL OR resource_id = :resourceId OR :resourceId = 0)"
            + "AND (:skillId IS NULL OR skill_id = :skillId OR :skillId = 0)"
            + "AND (:lower IS NULL OR experience >= :lower OR :lower = 0) "
            + "AND (:higher IS NULL OR experience <= :higher OR :higher = 0)", nativeQuery = true)
    Optional<List<ResourceSkill>> findByResourceAndSkillAndExperience(@Param("resourceId") int resourceId, @Param("skillId") int skillId, @Param("lower") int lower, @Param("higher") int higher);

    List<ResourceSkill> findAllByResourceId(Integer id);

    void deleteAllByResourceId(Integer id);
}
