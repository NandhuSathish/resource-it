/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.innovature.resourceit.repository;

import com.innovature.resourceit.entity.Resource;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author abdul.fahad
 */
public interface ResourceRepository extends JpaRepository<Resource, Integer>, PagingAndSortingRepository<Resource, Integer>, JpaSpecificationExecutor<Resource> {

    @Query("SELECT r FROM Resource r WHERE r.status = :status AND BINARY(r.email) = :email")
    Optional<Resource> findByEmailAndStatus(@Param("email") String email, @Param("status") byte status);

    Optional<Resource> findByRoleId(Integer roleId);

    Optional<Resource> findByIdAndStatus(Integer id, byte status);

    Optional<Resource> findByEmail(String email);

    Optional<Resource> findByEmailIgnoreCase(String email);

    Optional<Resource> findByDepartmentDepartmentId(Integer deptId);

    Optional<Resource> findByEmployeeId(int id);

    @Query("SELECT COUNT(r.id) FROM Resource r WHERE r.allocationStatus = :allocationStatus AND r.status = :status AND r.role.id <> :roleId")
    long countByAllocationStatusAndStatusAndNotRoleId(@Param("allocationStatus") Byte allocationStatus, @Param("status") Byte status, @Param("roleId") int roleId);

    @Query(value = "SELECT r.id, r.name, d.name, GROUP_CONCAT(p.name) AS project_names, r.experience, r.joining_date, r.email, r.allocation_status, ro.name, r.status, r.employee_id, ro.id ,d.department_id,r.last_worked_project_date "
            + "FROM resource r "
            + "LEFT JOIN allocation a ON r.id = a.resource_id AND a.allocation_expiry = 1 "
            + "LEFT JOIN project p ON a.project_id = p.project_id AND a.status=1 "
            + "LEFT JOIN role ro ON r.role_id = ro.id "
            + "LEFT JOIN department d ON r.department_id = d.department_id "
            + "WHERE (:name IS NULL OR r.name LIKE %:name% )"
            + "AND r.status=:status "
            + "AND (case when COALESCE(:departmentid) is null then r.department_id = r.department_id else (r.department_id in (:departmentid)) end) "
            + "AND (case when COALESCE(:roleid) is null then r.role_id = r.role_id else (r.role_id in (:roleid)) end) "
            + "AND (:employeeid IS NULL OR r.employee_id = :employeeid OR :employeeid = 0) "
            + " AND ((COALESCE(:lowerExp, 0) IS NULL AND COALESCE(:highExp, 0) IS NULL) OR r.experience BETWEEN COALESCE(:lowerExp, 0) AND COALESCE(:highExp, 0)) "
            + "AND (case when COALESCE(:allocationStatus) is null then r.allocation_status = r.allocation_status else (r.allocation_status in (:allocationStatus)) end) "
            + "GROUP BY r.id, r.name, d.name, r.experience, r.joining_date, r.email, r.allocation_status, ro.name, r.status, r.employee_id, ro.id, d.department_id ,r.last_worked_project_date ", nativeQuery = true)
    Page<Object[]> findAllByAllFilters(@Param("name") String name, @Param("departmentid") List<Integer> departmentId, @Param("employeeid") int employeeId,
                                       @Param("roleid") List<Integer> roleId, @Param("lowerExp") int lowerExp, @Param("highExp") int highExp,
                                       @Param("status") byte status,
                                       @Param("allocationStatus") List<Integer> allocationStatus,
                                       Pageable pageable);

    @Query(value = "SELECT r.id, r.name, d.name, GROUP_CONCAT(p.name) AS project_names, r.experience, r.joining_date, r.email, r.allocation_status, ro.name, r.status, r.employee_id, ro.id ,d.department_id,r.last_worked_project_date "
            + "FROM resource r "
            + "LEFT JOIN allocation a ON r.id = a.resource_id AND a.allocation_expiry = 1 "
            + "LEFT JOIN project p ON a.project_id = p.project_id AND a.status=1 "
            + "LEFT JOIN role ro ON r.role_id = ro.id "
            + "LEFT JOIN department d ON r.department_id = d.department_id "
            + "WHERE (:name IS NULL OR r.name LIKE %:name% ) "
            + "AND r.status=:status "
            + "AND (case when COALESCE(:departmentid) is null then r.department_id = r.department_id else (r.department_id in (:departmentid)) end) "
            + "AND (case when COALESCE(:roleid) is null then r.role_id = r.role_id else (r.role_id in (:roleid)) end) "
            + "AND (:employeeid IS NULL OR r.employee_id = :employeeid OR :employeeid = 0) "
            + "AND (case when COALESCE(:projectId) is null then p.project_id = p.project_id else (p.project_id in (:projectId)) end) "
            + " AND ((COALESCE(:lowerExp, 0) IS NULL AND COALESCE(:highExp, 0) IS NULL) OR r.experience BETWEEN COALESCE(:lowerExp, 0) AND COALESCE(:highExp, 0)) "
            + "AND (case when COALESCE(:allocationStatus) is null then r.allocation_status = r.allocation_status else (r.allocation_status in (:allocationStatus)) end) "
            + "GROUP BY r.id, r.name, d.name, r.experience, r.joining_date, r.email, r.allocation_status, ro.name, r.status, r.employee_id, ro.id, d.department_id ,r.last_worked_project_date ", nativeQuery = true)
    Page<Object[]> findAllByAllFiltersWithProjectName(@Param("name") String name, @Param("departmentid") List<Integer> departmentId, @Param("employeeid") int employeeId,
                                                      @Param("roleid") List<Integer> roleId, @Param("lowerExp") int lowerExp, @Param("highExp") int highExp, @Param("status") byte status,
                                                      @Param("allocationStatus") List<Integer> allocationStatus,
                                                      @Param("projectId") List<Integer> projectId, Pageable pageable);

    @Query(value = "SELECT r.id, r.name, d.name, GROUP_CONCAT(p.name) AS project_names,  r.experience, r.joining_date, r.email, r.allocation_status, ro.name, r.status, r.employee_id, ro.id ,d.department_id,r.last_worked_project_date "
            + "FROM resource r "
            + "LEFT JOIN allocation a ON r.id = a.resource_id AND a.allocation_expiry = 1 "
            + "LEFT JOIN project p ON a.project_id = p.project_id AND a.status=1 "
            + "LEFT JOIN role ro ON r.role_id = ro.id "
            + "LEFT JOIN department d ON r.department_id = d.department_id "
            + "WHERE (:name IS NULL OR r.name LIKE %:name% ) "
            + "AND r.status=:status "
            + "AND (case when COALESCE(:departmentid) is null then r.department_id = r.department_id else (r.department_id in (:departmentid)) end) "
            + "AND (case when COALESCE(:roleid) is null then r.role_id = r.role_id else (r.role_id in (:roleid)) end) "
            + "AND (:employeeid IS NULL OR r.employee_id = :employeeid OR :employeeid = 0) "
            + " AND ((COALESCE(:lowerExp, 0) IS NULL AND COALESCE(:highExp, 0) IS NULL) OR r.experience BETWEEN COALESCE(:lowerExp, 0) AND COALESCE(:highExp, 0)) "
            + "AND (case when COALESCE(:allocationStatus) is null then r.allocation_status = r.allocation_status else (r.allocation_status in (:allocationStatus)) end) "
            + "AND r.id in ("
            + "SELECT rs.resource_id FROM resource_skill rs INNER JOIN skill s ON s.skill_id = rs.skill_id "
            + "WHERE (rs.skill_id = :skillId1 AND rs.skill_id <> 0 AND rs.experience BETWEEN :lowExperience1 AND :highExperience1 AND rs.proficiency IN (:proficiency1)) "
            + "INTERSECT "
            + "SELECT rs.resource_id FROM resource_skill rs INNER JOIN skill s ON s.skill_id = rs.skill_id "
            + "WHERE (COALESCE(:skillId2, 0) = 0 OR (rs.skill_id = :skillId2 AND rs.skill_id <> 0 AND rs.experience BETWEEN :lowExperience2 AND :highExperience2 AND rs.proficiency IN (:proficiency2))) "
            + "INTERSECT "
            + "SELECT rs.resource_id FROM resource_skill rs INNER JOIN skill s ON s.skill_id = rs.skill_id "
            + "WHERE (COALESCE(:skillId3, 0) = 0 OR (rs.skill_id = :skillId3 AND rs.skill_id <> 0 AND rs.experience BETWEEN :lowExperience3 AND :highExperience3 AND rs.proficiency IN (:proficiency3))) "
            + "INTERSECT "
            + "SELECT rs.resource_id FROM resource_skill rs INNER JOIN skill s ON s.skill_id = rs.skill_id "
            + "WHERE (COALESCE(:skillId4, 0) = 0 OR (rs.skill_id = :skillId4 AND rs.skill_id <> 0 AND rs.experience BETWEEN :lowExperience4 AND :highExperience4 AND rs.proficiency IN (:proficiency4))) "
            + "INTERSECT "
            + "SELECT rs.resource_id FROM resource_skill rs INNER JOIN skill s ON s.skill_id = rs.skill_id "
            + "WHERE (COALESCE(:skillId5, 0) = 0 OR (rs.skill_id = :skillId5 AND rs.skill_id <> 0 AND rs.experience BETWEEN :lowExperience5 AND :highExperience5 AND rs.proficiency IN (:proficiency5))) "
            + ") "
            + "GROUP BY r.id, r.name, d.name, r.experience, r.joining_date, r.email, r.allocation_status, ro.name, r.status, r.employee_id, ro.id, d.department_id ,r.last_worked_project_date ", nativeQuery = true)
    Page<Object[]> findAllByAllFiltersWithSkillsAndExperience(@Param("name") String name, @Param("departmentid") List<Integer> departmentId, @Param("employeeid") int employeeId,
                                                              @Param("roleid") List<Integer> roleId, @Param("lowerExp") int lowerExp, @Param("highExp") int highExp,
                                                              @Param("status") byte status, @Param("allocationStatus") List<Integer> allocationStatus,
                                                              @Param("skillId1") Integer skillId1, @Param("lowExperience1") Integer lowExperience1, @Param("highExperience1") Integer highExperience1,
                                                              @Param("skillId2") Integer skillId2, @Param("lowExperience2") Integer lowExperience2, @Param("highExperience2") Integer highExperience2,
                                                              @Param("skillId3") Integer skillId3, @Param("lowExperience3") Integer lowExperience3, @Param("highExperience3") Integer highExperience3,
                                                              @Param("skillId4") Integer skillId4, @Param("lowExperience4") Integer lowExperience4, @Param("highExperience4") Integer highExperience4,
                                                              @Param("skillId5") Integer skillId5, @Param("lowExperience5") Integer lowExperience5, @Param("highExperience5") Integer highExperience5,
                                                              @Param("proficiency1") List<Byte> proficiency1,@Param("proficiency2") List<Byte> proficiency2,@Param("proficiency3") List<Byte> proficiency3,
                                                              @Param("proficiency4") List<Byte> proficiency4,@Param("proficiency5") List<Byte> proficiency5,
                                                              Pageable pageable);

    @Query(value = "SELECT r.id, r.name, d.name, GROUP_CONCAT(p.name) AS project_names,  r.experience, r.joining_date, r.email, r.allocation_status, ro.name, r.status, r.employee_id, ro.id ,d.department_id,r.last_worked_project_date "
            + "FROM resource r "
            + "LEFT JOIN allocation a ON r.id = a.resource_id AND a.allocation_expiry = 1 "
            + "LEFT JOIN project p ON a.project_id = p.project_id AND a.status=1 "
            + "LEFT JOIN role ro ON r.role_id = ro.id "
            + "LEFT JOIN department d ON r.department_id = d.department_id "
            + "WHERE (:name IS NULL OR r.name LIKE %:name% ) "
            + "AND r.status=:status "
            + "AND (case when COALESCE(:departmentid) is null then r.department_id = r.department_id else (r.department_id in (:departmentid)) end) "
            + "AND (case when COALESCE(:roleid) is null then r.role_id = r.role_id else (r.role_id in (:roleid)) end) "
            + "AND (:employeeid IS NULL OR r.employee_id = :employeeid OR :employeeid = 0) "
            + "AND (case when COALESCE(:projectId) is null then p.project_id = p.project_id else (p.project_id in (:projectId)) end) "
            + " AND ((COALESCE(:lowerExp, 0) IS NULL AND COALESCE(:highExp, 0) IS NULL) OR r.experience BETWEEN COALESCE(:lowerExp, 0) AND COALESCE(:highExp, 0)) "
            + "AND (case when COALESCE(:allocationStatus) is null then r.allocation_status = r.allocation_status else (r.allocation_status in (:allocationStatus)) end) "
            + "AND r.id in ( "
            + "SELECT rs.resource_id FROM resource_skill rs INNER JOIN skill s ON s.skill_id = rs.skill_id "
            + "WHERE (rs.skill_id = :skillId1 AND rs.skill_id <> 0 AND rs.experience BETWEEN :lowExperience1 AND :highExperience1 AND rs.proficiency IN (:proficiency1)) "
            + "INTERSECT "
            + "SELECT rs.resource_id FROM resource_skill rs INNER JOIN skill s ON s.skill_id = rs.skill_id "
            + "WHERE (COALESCE(:skillId2, 0) = 0 OR (rs.skill_id = :skillId2 AND rs.skill_id <> 0 AND rs.experience BETWEEN :lowExperience2 AND :highExperience2 AND rs.proficiency IN (:proficiency2))) "
            + "INTERSECT "
            + "SELECT rs.resource_id FROM resource_skill rs INNER JOIN skill s ON s.skill_id = rs.skill_id "
            + "WHERE (COALESCE(:skillId3, 0) = 0 OR (rs.skill_id = :skillId3 AND rs.skill_id <> 0 AND rs.experience BETWEEN :lowExperience3 AND :highExperience3 AND rs.proficiency IN (:proficiency3))) "
            + "INTERSECT "
            + "SELECT rs.resource_id FROM resource_skill rs INNER JOIN skill s ON s.skill_id = rs.skill_id "
            + "WHERE (COALESCE(:skillId4, 0) = 0 OR (rs.skill_id = :skillId4 AND rs.skill_id <> 0 AND rs.experience BETWEEN :lowExperience4 AND :highExperience4 AND rs.proficiency IN (:proficiency4))) "
            + "INTERSECT "
            + "SELECT rs.resource_id FROM resource_skill rs INNER JOIN skill s ON s.skill_id = rs.skill_id "
            + "WHERE (COALESCE(:skillId5, 0) = 0 OR (rs.skill_id = :skillId5 AND rs.skill_id <> 0 AND rs.experience BETWEEN :lowExperience5 AND :highExperience5 AND rs.proficiency IN (:proficiency5))) "
            + ") "
            + "GROUP BY r.id, r.name, d.name, r.experience, r.joining_date, r.email, r.allocation_status, ro.name, r.status, r.employee_id, ro.id, d.department_id ,r.last_worked_project_date ", nativeQuery = true)
    Page<Object[]> findAllByAllFiltersWithProjectNameSkillsAndExperience(@Param("name") String name, @Param("departmentid") List<Integer> departmentId, @Param("employeeid") int employeeId,
                                                                         @Param("roleid") List<Integer> roleId, @Param("lowerExp") int lowerExp, @Param("highExp") int highExp, @Param("status") byte status,
                                                                         @Param("allocationStatus") List<Integer> allocationStatus,
                                                                         @Param("skillId1") Integer skillId1, @Param("lowExperience1") Integer lowExperience1, @Param("highExperience1") Integer highExperience1,
                                                                         @Param("skillId2") Integer skillId2, @Param("lowExperience2") Integer lowExperience2, @Param("highExperience2") Integer highExperience2,
                                                                         @Param("skillId3") Integer skillId3, @Param("lowExperience3") Integer lowExperience3, @Param("highExperience3") Integer highExperience3,
                                                                         @Param("skillId4") Integer skillId4, @Param("lowExperience4") Integer lowExperience4, @Param("highExperience4") Integer highExperience4,
                                                                         @Param("skillId5") Integer skillId5, @Param("lowExperience5") Integer lowExperience5, @Param("highExperience5") Integer highExperience5,
                                                                         @Param("projectId") List<Integer> projectId,  @Param("proficiency1") List<Byte> proficiency1,@Param("proficiency2") List<Byte> proficiency2,@Param("proficiency3") List<Byte> proficiency3,
                                                                         @Param("proficiency4") List<Byte> proficiency4,@Param("proficiency5") List<Byte> proficiency5, Pageable pageable);

    @Query(value = "SELECT r.id, r.name, d.name, GROUP_CONCAT(p.name) AS project_names, r.experience, r.joining_date, r.email, r.allocation_status, ro.name, r.status, r.employee_id , r.last_worked_project_date "
            + "FROM resource r "
            + "LEFT JOIN allocation a ON r.id = a.resource_id AND a.allocation_expiry = 1 "
            + "LEFT JOIN project p ON a.project_id = p.project_id "
            + "LEFT JOIN role ro ON r.role_id = ro.id "
            + "LEFT JOIN department d ON r.department_id = d.department_id "
            + "WHERE (:name IS NULL OR r.name LIKE %:name% )"
            + "AND r.status=:status "
            + "AND (case when COALESCE(:departmentid) is null then r.department_id = r.department_id else (r.department_id in (:departmentid)) end) "
            + "AND (case when COALESCE(:roleid) is null then r.role_id = r.role_id else (r.role_id in (:roleid)) end) "
            + "AND (:employeeid IS NULL OR r.employee_id = :employeeid OR :employeeid = 0) "
            + " AND ((COALESCE(:lowerExp, 0) IS NULL AND COALESCE(:highExp, 0) IS NULL) OR r.experience BETWEEN COALESCE(:lowerExp, 0) AND COALESCE(:highExp, 0)) "
            + "AND (case when COALESCE(:allocationStatus) is null then r.allocation_status = r.allocation_status else (r.allocation_status in (:allocationStatus)) end) "
            + "GROUP BY r.id, r.name, d.name,  r.experience, r.joining_date, r.email, r.allocation_status, ro.name, r.status, r.employee_id, ro.id, d.department_id ,r.last_worked_project_date "
            + " ORDER BY joining_date DESC", nativeQuery = true)
    List<Object[]> findDownloadAllByAllFilters(@Param("name") String name, @Param("departmentid") List<Integer> departmentId, @Param("employeeid") int employeeId,
                                               @Param("roleid") List<Integer> roleId, @Param("lowerExp") int lowerExp, @Param("highExp") int highExp,
                                               @Param("status") byte status,
                                               @Param("allocationStatus") List<Integer> allocationStatus);

    @Query(value = "SELECT r.id, r.name, d.name, GROUP_CONCAT(p.name) AS project_names, r.experience, r.joining_date, r.email, r.allocation_status, ro.name, r.status, r.employee_id, r.last_worked_project_date "
            + "FROM resource r "
            + "LEFT JOIN allocation a ON r.id = a.resource_id AND a.allocation_expiry = 1 "
            + "LEFT JOIN project p ON a.project_id = p.project_id "
            + "LEFT JOIN role ro ON r.role_id = ro.id "
            + "LEFT JOIN department d ON r.department_id = d.department_id "
            + "WHERE (:name IS NULL OR r.name LIKE %:name% ) "
            + "AND r.status=:status "
            + "AND (case when COALESCE(:departmentid) is null then r.department_id = r.department_id else (r.department_id in (:departmentid)) end) "
            + "AND (case when COALESCE(:roleid) is null then r.role_id = r.role_id else (r.role_id in (:roleid)) end) "
            + "AND (:employeeid IS NULL OR r.employee_id = :employeeid OR :employeeid = 0) "
            + "AND (case when COALESCE(:projectId) is null then p.project_id = p.project_id else (p.project_id in (:projectId)) end) "
            + " AND ((COALESCE(:lowerExp, 0) IS NULL AND COALESCE(:highExp, 0) IS NULL) OR r.experience BETWEEN COALESCE(:lowerExp, 0) AND COALESCE(:highExp, 0)) "
            + "AND (case when COALESCE(:allocationStatus) is null then r.allocation_status = r.allocation_status else (r.allocation_status in (:allocationStatus)) end) "
            + "GROUP BY r.id, r.name, d.name,  r.experience, r.joining_date, r.email, r.allocation_status, ro.name, r.status, r.employee_id, ro.id, d.department_id ,r.last_worked_project_date "
            + "ORDER BY joining_date DESC", nativeQuery = true)
    List<Object[]> findDownloadAllByAllFiltersWithProjectName(@Param("name") String name, @Param("departmentid") List<Integer> departmentId, @Param("employeeid") int employeeId,
                                                              @Param("roleid") List<Integer> roleId, @Param("lowerExp") int lowerExp, @Param("highExp") int highExp, @Param("status") byte status,
                                                              @Param("allocationStatus") List<Integer> allocationStatus,
                                                              @Param("projectId") List<Integer> projectId);

    @Query(value = "SELECT r.id, r.name, d.name, GROUP_CONCAT(p.name) AS project_names, r.experience, r.joining_date, r.email, r.allocation_status, ro.name, r.status, r.employee_id, r.last_worked_project_date "
            + "FROM resource r "
            + "LEFT JOIN allocation a ON r.id = a.resource_id AND a.allocation_expiry = 1 "
            + "LEFT JOIN project p ON a.project_id = p.project_id "
            + "LEFT JOIN role ro ON r.role_id = ro.id "
            + "LEFT JOIN department d ON r.department_id = d.department_id "
            + "WHERE (:name IS NULL OR r.name LIKE %:name% ) "
            + "AND r.status=:status "
            + "AND (case when COALESCE(:departmentid) is null then r.department_id = r.department_id else (r.department_id in (:departmentid)) end) "
            + "AND (case when COALESCE(:roleid) is null then r.role_id = r.role_id else (r.role_id in (:roleid)) end) "
            + "AND (:employeeid IS NULL OR r.employee_id = :employeeid OR :employeeid = 0) "
            + "AND ((COALESCE(:lowerExp, 0) IS NULL AND COALESCE(:highExp, 0) IS NULL) OR r.experience BETWEEN COALESCE(:lowerExp, 0) AND COALESCE(:highExp, 0)) "
            + "AND (case when COALESCE(:allocationStatus) is null then r.allocation_status = r.allocation_status else (r.allocation_status in (:allocationStatus)) end) "
            + "AND r.id IN ("
            + "SELECT rs.resource_id FROM resource_skill rs INNER JOIN skill s ON s.skill_id = rs.skill_id "
            + "WHERE (rs.skill_id = :skillId1 AND rs.skill_id <> 0 AND rs.experience BETWEEN :lowExperience1 AND :highExperience1 AND rs.proficiency IN (:proficiency1)) "
            + "INTERSECT "
            + "SELECT rs.resource_id FROM resource_skill rs INNER JOIN skill s ON s.skill_id = rs.skill_id "
            + "WHERE (COALESCE(:skillId2, 0) = 0 OR (rs.skill_id = :skillId2 AND rs.skill_id <> 0 AND rs.experience BETWEEN :lowExperience2 AND :highExperience2 AND rs.proficiency IN (:proficiency2))) "
            + "INTERSECT "
            + "SELECT rs.resource_id FROM resource_skill rs INNER JOIN skill s ON s.skill_id = rs.skill_id "
            + "WHERE (COALESCE(:skillId3, 0) = 0 OR (rs.skill_id = :skillId3 AND rs.skill_id <> 0 AND rs.experience BETWEEN :lowExperience3 AND :highExperience3 AND rs.proficiency IN (:proficiency3))) "
            + "INTERSECT "
            + "SELECT rs.resource_id FROM resource_skill rs INNER JOIN skill s ON s.skill_id = rs.skill_id "
            + "WHERE (COALESCE(:skillId4, 0) = 0 OR (rs.skill_id = :skillId4 AND rs.skill_id <> 0 AND rs.experience BETWEEN :lowExperience4 AND :highExperience4 AND rs.proficiency IN (:proficiency4))) "
            + "INTERSECT "
            + "SELECT rs.resource_id FROM resource_skill rs INNER JOIN skill s ON s.skill_id = rs.skill_id "
            + "WHERE (COALESCE(:skillId5, 0) = 0 OR (rs.skill_id = :skillId5 AND rs.skill_id <> 0 AND rs.experience BETWEEN :lowExperience5 AND :highExperience5 AND rs.proficiency IN (:proficiency5))) "
            + ") "
            + "GROUP BY r.id, r.name, d.name, r.experience, r.joining_date, r.email, r.allocation_status, ro.name, r.status, r.employee_id, ro.id, d.department_id, r.last_worked_project_date "
            + "ORDER BY joining_date DESC", nativeQuery = true)
    List<Object[]> findDownloadAllByAllFiltersWithSkillsAndExperience(@Param("name") String name, @Param("departmentid") List<Integer> departmentId, @Param("employeeid") int employeeId,
                                                                      @Param("roleid") List<Integer> roleId, @Param("lowerExp") int lowerExp, @Param("highExp") int highExp,
                                                                      @Param("status") byte status, @Param("allocationStatus") List<Integer> allocationStatus,
                                                                      @Param("skillId1") Integer skillId1, @Param("lowExperience1") Integer lowExperience1, @Param("highExperience1") Integer highExperience1,
                                                                      @Param("skillId2") Integer skillId2, @Param("lowExperience2") Integer lowExperience2, @Param("highExperience2") Integer highExperience2,
                                                                      @Param("skillId3") Integer skillId3, @Param("lowExperience3") Integer lowExperience3, @Param("highExperience3") Integer highExperience3,
                                                                      @Param("skillId4") Integer skillId4, @Param("lowExperience4") Integer lowExperience4, @Param("highExperience4") Integer highExperience4,
                                                                      @Param("skillId5") Integer skillId5, @Param("lowExperience5") Integer lowExperience5, @Param("highExperience5") Integer highExperience5,
                                                                      @Param("proficiency1") List<Byte> proficiency1,@Param("proficiency2") List<Byte> proficiency2,@Param("proficiency3") List<Byte> proficiency3,
                                                                      @Param("proficiency4") List<Byte> proficiency4,@Param("proficiency5") List<Byte> proficiency5);

    @Query(value = "SELECT r.id, r.name, d.name, GROUP_CONCAT(p.name) AS project_names, r.experience, r.joining_date, r.email, r.allocation_status, ro.name, r.status, r.employee_id, r.last_worked_project_date "
            + "FROM resource r "
            + "LEFT JOIN allocation a ON r.id = a.resource_id AND a.allocation_expiry = 1 "
            + "LEFT JOIN project p ON a.project_id = p.project_id "
            + "LEFT JOIN role ro ON r.role_id = ro.id "
            + "LEFT JOIN department d ON r.department_id = d.department_id "
            + "WHERE (:name IS NULL OR r.name LIKE %:name% ) "
            + "AND r.status=:status "
            + "AND (case when COALESCE(:departmentid) is null then r.department_id = r.department_id else (r.department_id in (:departmentid)) end) "
            + "AND (case when COALESCE(:roleid) is null then r.role_id = r.role_id else (r.role_id in (:roleid)) end) "
            + "AND (:employeeid IS NULL OR r.employee_id = :employeeid OR :employeeid = 0) "
            + "AND (case when COALESCE(:projectId) is null then p.project_id = p.project_id else (p.project_id in (:projectId)) end) "
            + " AND ((COALESCE(:lowerExp, 0) IS NULL AND COALESCE(:highExp, 0) IS NULL) OR r.experience BETWEEN COALESCE(:lowerExp, 0) AND COALESCE(:highExp, 0)) "
            + "AND (case when COALESCE(:allocationStatus) is null then r.allocation_status = r.allocation_status else (r.allocation_status in (:allocationStatus)) end) "
            + "AND r.id in ( "
            + "SELECT rs.resource_id FROM resource_skill rs INNER JOIN skill s ON s.skill_id = rs.skill_id "
            + "WHERE (rs.skill_id = :skillId1 AND rs.skill_id <> 0 AND rs.experience BETWEEN :lowExperience1 AND :highExperience1 AND rs.proficiency IN (:proficiency1)) "
            + "INTERSECT "
            + "SELECT rs.resource_id FROM resource_skill rs INNER JOIN skill s ON s.skill_id = rs.skill_id "
            + "WHERE (COALESCE(:skillId2, 0) = 0 OR (rs.skill_id = :skillId2 AND rs.skill_id <> 0 AND rs.experience BETWEEN :lowExperience2 AND :highExperience2 AND rs.proficiency IN (:proficiency2))) "
            + "INTERSECT "
            + "SELECT rs.resource_id FROM resource_skill rs INNER JOIN skill s ON s.skill_id = rs.skill_id "
            + "WHERE (COALESCE(:skillId3, 0) = 0 OR (rs.skill_id = :skillId3 AND rs.skill_id <> 0 AND rs.experience BETWEEN :lowExperience3 AND :highExperience3 AND rs.proficiency IN (:proficiency3))) "
            + "INTERSECT "
            + "SELECT rs.resource_id FROM resource_skill rs INNER JOIN skill s ON s.skill_id = rs.skill_id "
            + "WHERE (COALESCE(:skillId4, 0) = 0 OR (rs.skill_id = :skillId4 AND rs.skill_id <> 0 AND rs.experience BETWEEN :lowExperience4 AND :highExperience4 AND rs.proficiency IN (:proficiency4))) "
            + "INTERSECT "
            + "SELECT rs.resource_id FROM resource_skill rs INNER JOIN skill s ON s.skill_id = rs.skill_id "
            + "WHERE (COALESCE(:skillId5, 0) = 0 OR (rs.skill_id = :skillId5 AND rs.skill_id <> 0 AND rs.experience BETWEEN :lowExperience5 AND :highExperience5 AND rs.proficiency IN (:proficiency5))) "
            + ") "
            + "GROUP BY r.id, r.name, d.name, r.experience, r.joining_date, r.email, r.allocation_status, ro.name, r.status, r.employee_id, ro.id, d.department_id ,r.last_worked_project_date, p.name, a.allocation_expiry "
            + " ORDER BY joining_date DESC", nativeQuery = true)
    List<Object[]> findDownloadAllByAllFiltersWithProjectNameSkillsAndExperience(@Param("name") String name, @Param("departmentid") List<Integer> departmentId, @Param("employeeid") int employeeId,
                                                                                 @Param("roleid") List<Integer> roleId, @Param("lowerExp") int lowerExp, @Param("highExp") int highExp, @Param("status") byte status,
                                                                                 @Param("allocationStatus") List<Integer> allocationStatus,
                                                                                 @Param("skillId1") Integer skillId1, @Param("lowExperience1") Integer lowExperience1, @Param("highExperience1") Integer highExperience1,
                                                                                 @Param("skillId2") Integer skillId2, @Param("lowExperience2") Integer lowExperience2, @Param("highExperience2") Integer highExperience2,
                                                                                 @Param("skillId3") Integer skillId3, @Param("lowExperience3") Integer lowExperience3, @Param("highExperience3") Integer highExperience3,
                                                                                 @Param("skillId4") Integer skillId4, @Param("lowExperience4") Integer lowExperience4, @Param("highExperience4") Integer highExperience4,
                                                                                 @Param("skillId5") Integer skillId5, @Param("lowExperience5") Integer lowExperience5, @Param("highExperience5") Integer highExperience5,
                                                                                 @Param("projectId") List<Integer> projectId,
                                                                                 @Param("proficiency1") List<Byte> proficiency1,@Param("proficiency2") List<Byte> proficiency2,@Param("proficiency3") List<Byte> proficiency3,
                                                                                 @Param("proficiency4") List<Byte> proficiency4,@Param("proficiency5") List<Byte> proficiency5);

    List<Resource> findByRoleIdInAndStatus(List<Integer> roleId, Byte status);

    @Query("SELECT r.id FROM Resource r WHERE r.role.id = :roleId AND r.status = :status")
    List<Integer> findAllIdByRoleIdAndStatus(@Param("roleId") Integer roleId, @Param("status") Byte status);

    List<Resource> findAllByStatus(byte status);
}
