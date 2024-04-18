/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.innovature.resourceit.repository;

import com.innovature.resourceit.entity.Allocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author abdul.fahad
 */
@Repository
public interface AllocationRepository extends JpaRepository<Allocation, Integer> {

    List<Allocation> findByProjectProjectIdAndStatus(Integer projectId, byte status);

    @Query(value = "select * from allocation where resource_id =:id AND status =:status", nativeQuery = true)
    List<Allocation> findByResourceIdAndStatus(@Param("id") int id, @Param("status") byte status);

    List<Allocation> findAllByResourceIdAndStartDateBeforeAndEndDateAfterAndStatus(Integer resourceId, Date endDate, Date startDate, byte status);

    Optional<Allocation> findByIdAndStatus(Integer allocationId, Byte status);

    List<Allocation> findAllByResourceIdAndStatus(Integer resourceId, byte status);

    Page<Allocation> findAllByProjectProjectIdAndStatusAndAllocationExpiryInOrderByUpdatedDateDesc(int id, byte status, List<Byte> expiryStatusValues, Pageable pageable);

    List<Allocation> findAllByProjectProjectId(int id);

    List<Allocation> findAllByResourceIdAndEndDateAndStatus(Integer resourceId, Date startDate, byte value);

    List<Allocation> findAllByResourceIdAndStartDateAndStatus(Integer resourceId, Date endDate, byte value);

    List<Allocation> findByResourceIdAndAllocationExpiryInAndStatusAndProjectProjectTypeNot(Integer resourceId, List<Byte> expiryValues, byte value1, byte value2);

    List<Allocation> findByProjectProjectIdAndAllocationExpiryInAndStatus(Integer projectId, List<Byte> expiryValues, byte value1);

    @Query(value = "SELECT * FROM allocation WHERE end_date BETWEEN CURDATE()  AND CURDATE() + INTERVAL 7 day AND  project_id=:projectId AND status=:status", nativeQuery = true)
    List<Allocation> findAllAllocationFallsBetween(@Param("projectId") Integer projectId, @Param("status") byte status);

    Allocation findTopByProjectProjectIdAndResourceIdAndStatusOrderByCreatedDateDesc(Integer projectId, Integer managerId, byte status);

    Optional<Allocation> findTopByResourceIdAndProjectProjectTypeNotAndAllocationExpiryAndStatusOrderByEndDateDesc(Integer resourceId, Byte projectType, Byte allocationExpiryStatus, Byte status);

    List<Allocation> findByResourceIdAndStatusAndAllocationExpiryNot(Integer resourceId, Byte status, Byte expiryStatus);

    Allocation findTopByResourceIdAndStatusAndAllocationExpiryIn(Integer resourceId, Byte status, Byte[] allocationExpiry);

    @Query("SELECT COUNT(DISTINCT a.resource.id) " +
            "FROM Allocation a " +
            "WHERE a.project.projectId = :projectId " +
            "AND a.allocationExpiry IN (:expiryValues) " +
            "AND a.status = :status")
    int countDistinctResourceIdByProjectProjectIdAndAllocationExpiryInAndStatus(
            @Param("projectId") Integer projectId,
            @Param("expiryValues") List<Byte> expiryValues,
            @Param("status") Byte status);

    List<Allocation> findAllByResourceIdAndStartDateBeforeAndEndDateAfterAndStatusAndProjectProjectTypeNot(Integer id, Date endDate, Date startDate, byte value, Byte value1);

    List<Allocation> findAllByResourceIdAndStartDateAndStatusAndProjectProjectTypeNot(Integer id, Date endDate, byte value, Byte value1);

    List<Allocation> findAllByResourceIdAndEndDateAndStatusAndProjectProjectTypeNot(Integer id, Date startDate, byte value, Byte value1);
}
