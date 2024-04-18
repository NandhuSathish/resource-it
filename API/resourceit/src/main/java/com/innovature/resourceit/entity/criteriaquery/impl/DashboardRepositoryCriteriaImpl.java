/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.innovature.resourceit.entity.criteriaquery.impl;

import com.innovature.resourceit.entity.criteriaquery.DashboardRepositoryCriteria;
import com.innovature.resourceit.entity.dto.responsedto.DashboardChartResponseDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author abdul.fahad
 */
@Repository
public class DashboardRepositoryCriteriaImpl implements DashboardRepositoryCriteria {
  @Autowired EntityManager entityManager;


  @Override
  public DashboardChartResponseDTO findByDepartmentAllocationStatusIn(
      List<Integer> allocationStatusInt) {
    StringBuilder nativeQuery =
        new StringBuilder(
            "SELECT  d.name as departmentName, count(r.id) as count FROM resource r , department d where r.department_id = d.department_id and r.status = 1 and r.role_id <> 1 ");
    if (allocationStatusInt != null && !allocationStatusInt.isEmpty()) {
      nativeQuery.append("and r.allocation_status in :allocationStatus ");
    }
    nativeQuery.append("group by r.department_id order by d.display_order");

    Query query = entityManager.createNativeQuery(nativeQuery.toString(), Object.class);
    if (allocationStatusInt != null && !allocationStatusInt.isEmpty()) {
      query.setParameter("allocationStatus", allocationStatusInt);
    }
    List<?> os = query.getResultList();
    DashboardChartResponseDTO dashboardChartResponseDTO = new DashboardChartResponseDTO();
    List<String> labels = new LinkedList<>();
    List<Integer> datas = new LinkedList<>();
    for (Object o : os) {
      Object[] result = (Object[]) o;
      labels.add((String) result[0]);
      datas.add(((Number) result[1]).intValue());
    }
    dashboardChartResponseDTO.setLabels(labels);
    dashboardChartResponseDTO.setDatas(datas);

    return dashboardChartResponseDTO;
  }

  public DashboardChartResponseDTO findByIdInSkillAllocationStatusIn(List<Integer> skillIds, List<Integer> allocationStatusInt) {
      String nativeQuery = createNativeQuery(allocationStatusInt);

      Query query = entityManager.createNativeQuery(nativeQuery, Object.class);
      if (allocationStatusInt != null && !allocationStatusInt.isEmpty()) {
          query.setParameter("allocationStatus", allocationStatusInt);
      }
      if (skillIds != null && !skillIds.isEmpty()) {
          query.setParameter("skillIds", skillIds);
      }
      List<?> os = query.getResultList();
      DashboardChartResponseDTO dashboardChartResponseDTO = new DashboardChartResponseDTO();
      List<String> labels = Arrays.asList("Benched", "Internal", "Billable");
      List<Integer> datas = Arrays.asList(0, 0, 0);
      for (Object o : os) {
        Object[] result = (Object[]) o;
      int index = labels.indexOf(result[0]);
        if (index != -1) {
          datas.set(index, (result[1] != null) ? ((Number) result[1]).intValue() : 0);
        }
      }
      dashboardChartResponseDTO.setLabels(labels);
      dashboardChartResponseDTO.setDatas(datas);
      boolean allZeros = dashboardChartResponseDTO.getDatas().stream().allMatch(value -> value == 0);

      if (allZeros) {
          return new DashboardChartResponseDTO(Collections.emptyList(), Collections.emptyList());
      } else {
          return dashboardChartResponseDTO;
      }
  }
  private String createNativeQuery(List<Integer> allocationStatusInt) {
      StringBuilder nativeQuery = new StringBuilder("SELECT l.labels, COALESCE(d.data, 0) FROM (SELECT 'Benched' AS labels UNION SELECT 'Internal' UNION SELECT 'Billable') AS l LEFT JOIN (");

      nativeQuery.append("SELECT 'Benched' AS labels, CAST(SUM(CASE WHEN r.allocation_status = 0 THEN 1 ELSE 0 END) AS SIGNED) AS data FROM resource r JOIN resource_skill rs ON r.id = rs.resource_id JOIN skill s ON rs.skill_id = s.skill_id WHERE r.status = 1 AND r.role_id <> 1 AND s.skill_id IN (:skillIds)");

      if (allocationStatusInt != null && !allocationStatusInt.isEmpty()) {
        nativeQuery.append(" UNION SELECT 'Internal' AS labels, CAST(SUM(CASE WHEN r.allocation_status = 1 THEN 1 ELSE 0 END) AS SIGNED) AS data FROM resource r JOIN resource_skill rs ON r.id = rs.resource_id JOIN skill s ON rs.skill_id = s.skill_id WHERE r.status = 1 AND r.role_id <> 1 AND s.skill_id IN (:skillIds) ");
        nativeQuery.append(" UNION SELECT 'Billable' AS labels, COALESCE(CAST(SUM(CASE WHEN r.allocation_status = 2 THEN 1 ELSE 0 END) AS SIGNED), 0) AS data FROM resource r JOIN resource_skill rs ON r.id = rs.resource_id JOIN skill s ON rs.skill_id = s.skill_id WHERE r.status = 1 AND r.role_id <> 1 AND s.skill_id IN (:skillIds) AND r.allocation_status IN (:allocationStatus) ");
      }

      nativeQuery.append(") AS d ON l.labels = d.labels");

      return nativeQuery.toString();
  }








}
