/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.innovature.resourceit.service.impli;

import com.innovature.resourceit.entity.ResourceSkill;
import com.innovature.resourceit.entity.dto.responsedto.ResourceSkillResponseDTO;
import com.innovature.resourceit.exceptionhandler.BadRequestException;
import com.innovature.resourceit.repository.ResourceSkillRepository;
import com.innovature.resourceit.repository.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author abdul.fahad
 */
@Service
public class CommonServiceForResourceDownloadAndListingImpli {

  @Autowired MessageSource messageSource;

  @Autowired private SkillRepository skillRepository;

  @Autowired private ResourceSkillRepository resourceSkillRepository;

  public String getExperienceInStringFormat(int val) {
    int year = val / 12;
    int month = val - year * 12;
    return year + "." + month;
  }
  public Date getDateFromStringHyphen(String d) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Date date = null;
    try {
      date = sdf.parse(d);
    } catch (ParseException p) {
      throw new BadRequestException(
          messageSource.getMessage("DATE_FORMAT_ERROR", null, Locale.ENGLISH));
    }
    return date;
  }
  public String joiningDateStringFormat(Object date) throws ParseException {
    // 2023-09-11 14:41:57.618
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    Date d = sdf1.parse(date.toString());
    return sdf.format(d);
  }

  public List<ResourceSkillResponseDTO> setResourceSkillResponse(int resourceId) {
    List<ResourceSkill> resourceSkills = resourceSkillRepository.findAllByResourceId(resourceId);
    List<ResourceSkill> sortedResourceSkills =
        resourceSkills.stream()
            .sorted(Comparator.comparing(ResourceSkill::getExperience).reversed())
            .toList();

    // Mapping to ResourceSkillResponseDTO
    return sortedResourceSkills.stream().map(ResourceSkillResponseDTO::new).toList();
  }



  public String skillAndExp(List<ResourceSkillResponseDTO> resourceSkillResponseDTOS) {
    StringBuilder sb = new StringBuilder();
    for (ResourceSkillResponseDTO resourceSkillResponseDTO : resourceSkillResponseDTOS) {
      String proficiency;
      if(resourceSkillResponseDTO.getProficiency()==0){
        proficiency="Beginner";
      } else if (resourceSkillResponseDTO.getProficiency()==1) {
        proficiency="Intermediate";

      }
      else {
        proficiency="Expert";
      }
      sb.append(resourceSkillResponseDTO.getSkillName())
          .append(" : ")
          .append(getExperienceInStringFormat(resourceSkillResponseDTO.getExperience()))
          .append("Y : ")

      .append(proficiency);

      sb.append(", ");
    }
    if (!resourceSkillResponseDTOS.isEmpty()) {
      sb.setLength(sb.length() - 2);
    }
    return sb.toString();
  }

  public void checkLowerExpLessThanHighExp(Integer lowerExp, Integer highExp) {
    if (lowerExp != null && highExp != null && (lowerExp > highExp)) {
      throw new BadRequestException(
          messageSource.getMessage("LOW_HIGH_EXPERIENCE", null, Locale.ENGLISH));
    }
  }
}
