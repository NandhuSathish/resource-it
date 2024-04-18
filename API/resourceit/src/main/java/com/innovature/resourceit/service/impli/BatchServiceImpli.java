package com.innovature.resourceit.service.impli;

import com.innovature.resourceit.entity.Allocation;
import com.innovature.resourceit.entity.Department;
import com.innovature.resourceit.entity.Project;
import com.innovature.resourceit.entity.Resource;
import com.innovature.resourceit.exceptionhandler.BadRequestException;
import com.innovature.resourceit.repository.AllocationRepository;
import com.innovature.resourceit.repository.ProjectRepository;
import com.innovature.resourceit.repository.ResourceRepository;
import com.innovature.resourceit.service.BatchService;
import com.innovature.resourceit.util.CommonFunctions;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class BatchServiceImpli implements BatchService {

    @Autowired
    ResourceRepository resourceRepository;

    @Autowired
    AllocationRepository allocationRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    CommonFunctions commonFunctions;

    @Value("${batch.limit}")
    public int maxBatchLimit;

    @Autowired
    public JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    public String sendEmail;
    private static final Logger LOGGER = LoggerFactory.getLogger(BatchServiceImpli.class);

    //for updating resource allocation status and allocation expiry status and also project state and team size.
//and current experience for each resource
    @Override
    public void updateAllocationStatusExpiryAndTeamSize() {
        this.commonFunctions.scheduleUpdateAllocationStatusExpiryAndTeamSize();
        updateCurrentExperience();
    }

    private void updateCurrentExperience() {
        LocalDate currentDate = LocalDate.now();
        List<Resource> resourceList = resourceRepository.findAllByStatus(Resource.Status.ACTIVE.value);

        List<Resource> updatedResources = resourceList.stream().map(resource -> {
            LocalDate joiningDate = this.commonFunctions.convertDateToTimestamp(resource.getJoiningDate()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            int prevExperience = resource.getPrevExperience();
            int monthsOfExperience = (int) Period.between(joiningDate, currentDate).toTotalMonths();
            monthsOfExperience = Math.max(monthsOfExperience, 0);
            int currentExperience = prevExperience + monthsOfExperience;
            resource.setExperience(currentExperience);
            return resource;
        }).toList();
        resourceRepository.saveAll(updatedResources);
    }


    @Override
    public void sendReminder() {
        List<Integer> managerIdList = projectRepository.findAllManagerByProject(Project.statusValues.ACTIVE.value);
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();
        LocalDate localCurrentDate = currentDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        String signature = "<table style=\"color:rgb(0,0,0);font-family:&quot;Times New Roman&quot;;font-size:medium;width:483px;\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\">\n"
                + "  <tbody>\n"
                + "   \n"
                + "    <tr>\n"
                + "      <td style=\"font-size:10pt;font-family:Arial;width:183px;\" valign=\"middle\"><a href=\"http://innovature.ai/\" target=\"_blank\"><img src=\"https://innovature.ai/wp-content/uploads/2020/03/innovature-logo.png\" alt=\"innovaturelabs\" class=\"CToWUd\" border=\"0\" style=\"height:65px;\"></a></td>\n"
                + "      <td style=\"font-size:10pt;font-family:Arial;width:400px;padding-left:20px;border-left:1px solid rgb(20,95,177)\" valign=\"top\">\n"
                + "        <strong>t:</strong><span style=\"padding-left:11px\">+91-484-4038120</span><br><strong>w:</strong>&nbsp;<a href=\"https://innovature.ai/\" style=\"color:rgb(22,141,203);text-decoration-line:none\" target=\"_blank\"><span style=\"padding-left:4px\">https://innovature.ai</span></a>\n"
                + "        <div style=\"padding-top:3px\"><a href=\"https://www.facebook.com/innovature/\" target=\"_blank\"><img src=\"https://innovature.ai/wp-content/uploads/2020/06/facebook.png\" class=\"CToWUd\" border=\"0\" style=\"height:16px;\"></a>&nbsp;&nbsp;<a href=\"https://in.linkedin.com/company/innovature-labs\" target=\"_blank\"><img src=\"https://innovature.ai/wp-content/uploads/2020/06/linkdin.png\" class=\"CToWUd\" border=\"0\" style=\"height:16px;\"></a>&nbsp;&nbsp;<a href=\"https://twitter.com/Innovature_ai\" target=\"_blank\"><img src=\"https://innovature.ai/wp-content/uploads/2020/06/twitter-1.png\" class=\"CToWUd\" border=\"0\" style=\"height:16px;\"></a>&nbsp;&nbsp;<a href=\"https://www.google.com/maps/place/Innovature+Labs/@10.0039836,76.3748932,19.25z/data=!4m5!3m4!1s0x3b080c5cfabc4eb7:0xee6880f8c72caaf3!8m2!3d10.0040958!4d76.3756261\" target=\"_blank\"><img src=\"\n"
                + "https://innovature.ai/wp-content/uploads/2017/07/map-pin.png\" class=\"CToWUd\" border=\"0\" style=\"height:16px;\"></a>&nbsp;</div>\n"
                + "      </td>\n"
                + "<td></td>\n"
                + "<td style=\"font-size:10pt;font-family:Arial;width:400px; padding-left: 20px; padding-bottom:10px;\" valign=\"middle\">\n"
                + "\t  \n"
                + "\t  <img src=\"https://drive.google.com/uc?export=view&id=1YnIUnu9hXulNHGa1jAZqRom62YV3Qsok\" alt=\"innovaturelabs\" class=\"CToWUd\" border=\"0\" style=\" width:475px;\">\n"
                + "   \n"
                + "\t</td>\n"
                + "\n"
                + "    </tr>\n"
                + "  </tbody>\n"
                + "</table>\n"
                + "<span style=\"color:#666666;font-size:x-small\"><i>Disclaimer:- The information contained in this electronic message and any attachments to this message are intended for the exclusive use of the addressee(s) and may contain proprietary, confidential or privileged information. If you are not the intended recipient, you should not disseminate, distribute or copy this email. Please notify the sender immediately and destroy all copies of this message and any attachments. The views expressed in this email message (including the enclosure/(s) or attachment/(s) if any) are those of the individual sender, except where the sender expressly, and with authority, states them to be the views of Innovature. Before opening any mail and attachments please check them for viruses. Innovature does not accept any liability for virus-infected emails.</i><br></span>\n";

        for (Integer x : managerIdList) {
            Optional<Resource> optionalResource = resourceRepository.findByIdAndStatus(x, Resource.Status.ACTIVE.value);
            if (optionalResource.isPresent()) {
                Resource resource = optionalResource.get();
                try {
                    MimeMessage message4 = mailSender.createMimeMessage();
                    MimeMessageHelper helper = new MimeMessageHelper(message4);
                    helper.setFrom(sendEmail);
                    helper.setSubject("Project Allocation Expiry Reminder! ");
                    helper.setTo(resource.getEmail());
                    boolean html = true;
                    List<Project> projectList = projectRepository.findByManagerIdAndStatusAndProjectStateNotAndEdited(resource.getId(), Project.statusValues.ACTIVE.value, Project.projectStateValues.COMPLETED.value, Project.editedValues.NOT_EDITED.value);

                    String body = checkingAllocation(projectList, localCurrentDate, currentDate);

                    helper.setText("<form >Hello  "
                            + resource.getName() + ","
                            + "<br>"
                            + body
                            + "<br><b>Regards,</b>" + "<br><b>Resource&nbsp;Manager</b></form><br>" + signature, html);
                    mailSender.send(message4);

                } catch (Exception e) {
                    LOGGER.error(String.valueOf(e));
                }
            }
        }
    }

    public String checkingAllocation(List<Project> projectList, LocalDate localCurrentDate, Date currentDate) {
        String starter1 = "There are no allocations approaching expiration";
        String starter2 = "The following allocations are going to expire soon! <br>";
        String td = "</td >";
        String body = "";
        boolean flag = true;
        if (!projectList.isEmpty()) {
            StringBuilder bodyBuilder = new StringBuilder();
            for (Project p : projectList) {
                List<Allocation> allocations = allocationRepository.findAllAllocationFallsBetween(p.getProjectId(), Allocation.StatusValues.ACTIVE.value);
                List<Allocation> modifiedAllocations = allocations.stream().filter(allocation -> !Objects.equals(allocation.getResource().getId(), p.getManager().getId())).toList();
                if (!modifiedAllocations.isEmpty()) {
                    flag = false;

                    bodyBuilder.append("<br>Project&nbsp;Name:&nbsp;").append(p.getName())
                            .append("<br><br><table style=\"border-collapse: collapse; width: 600px;table-layout: fixed;\"><thead>")
                            .append("            <tr>")
                            .append("                <th style=\"border: 1px solid black; padding: 8px;width: 170px;\">Resource Name<br>[Employee Id]</th>")
                            .append("                <th style=\"border: 1px solid black; padding: 8px;width: 170px;\">Department</th>")
                            .append("                <th style=\"border: 1px solid black; padding: 8px;width: 120px;\">Remaining Days</th>")
                            .append("                <th style=\"border: 1px solid black; padding: 8px;width: 150px;\">Start Date</th>")
                            .append("                <th style=\"border: 1px solid black; padding: 8px;width: 150px;\">Release Date</th>")
                            .append("            </tr>")
                            .append("        </thead>");

                    for (Allocation allocation : modifiedAllocations) {
                        Optional<Resource> optionalR = resourceRepository.findByIdAndStatus(allocation.getResource().getId(), Resource.Status.ACTIVE.value);
                        if (optionalR.isPresent()) {
                            Department department = optionalR.get().getDepartment();
                            Integer daysLeft = 0;
                            String formattedStartDate = "";
                            String formattedEndDate = "";
                            Date utilEndDate = allocation.getEndDate();
                            Date utilStartDate = allocation.getStartDate();
                            LocalDate localEndDate = this.commonFunctions.convertDateToTimestamp(utilEndDate).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                            LocalDate localStartDate = this.commonFunctions.convertDateToTimestamp(utilStartDate).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                            Date allocationEndDate = Date.from(localEndDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                            Date allocationStartDate = Date.from(localStartDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

                            formattedStartDate = dateFormater(allocationStartDate);
                            formattedEndDate = dateFormater(allocationEndDate);

                            daysLeft = dateFilter(localStartDate, localCurrentDate, allocationStartDate, allocationEndDate, currentDate);

                            bodyBuilder.append("<tbody >")
                                    .append(" <tr>")
                                    .append("<td style=\"border: 1px solid black; padding: 8px;width: 170px;text-align: center;word-wrap: break-word;\">")
                                    .append(optionalR.get().getName()).append("<br>[").append(optionalR.get().getEmployeeId()).append("]</td >")
                                    .append("<td style=\"border: 1px solid black; padding: 8px;width: 170px;text-align: center;\">")
                                    .append(department.getName()).append(td)
                                    .append("<td style=\"border: 1px solid black; padding: 8px;width: 120px;text-align: center;color:red;font-weight: bold;\">")
                                    .append(daysLeft).append(td)
                                    .append("<td style=\"border: 1px solid black; padding: 8px;width: 150px;text-align: center;\">")
                                    .append(formattedStartDate).append(td)
                                    .append("<td style=\"border: 1px solid black; padding: 8px;width: 150px;text-align: center;\">")
                                    .append(formattedEndDate).append(td)
                                    .append("</tr >")
                                    .append("</tbody >");
                        }

                    }
                    body = bodyBuilder.toString();
                }
            }

        }

        body = projectFilterWithFlag(body, projectList, flag, starter1, starter2);
        return body;
    }

    public String projectFilterWithFlag(String body, List<Project> projectList, boolean flag, String starter1, String starter2) {
        if (projectList.isEmpty() || flag) {
            throw new BadRequestException(starter1);
        } else {
            body = starter2 + body
                    + "<br> <a href=\"https://resourceit-dev.innovaturelabs.com/projectManagement\">View&nbsp;Allocation&nbsp;Details</a><br>";
        }
        return body;
    }

    public Integer dateFilter(LocalDate localStartDate, LocalDate localCurrentDate,
                              Date allocationStartDate, Date allocationEndDate, Date currentDate) {
        Integer daysLeft = 0;
        if (localStartDate.isAfter(localCurrentDate)) {
            daysLeft = calculateWorkDayModified(allocationStartDate,
                    allocationEndDate) + 1;
        } else {
            daysLeft = calculateWorkDayModified(currentDate,
                    allocationEndDate);
        }
        return daysLeft;
    }

    public String dateFormater(Date dateToFormat) {
        LocalDate localDate = dateToFormat.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return localDate.format(outputFormatter);
    }

    public Integer calculateWorkDayModified(Date startDate, Date endDate) {
        int numberOfDays = 0;
        Instant instant1 = startDate.toInstant();
        Instant instant2 = endDate.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDate localStartDate = instant1.atZone(zoneId).toLocalDate();
        LocalDate localEndDate = instant2.atZone(zoneId).toLocalDate();
        while (localStartDate.isBefore(localEndDate)) {
            if (!commonFunctions.isWeekend(localStartDate) && !commonFunctions.isHoliday(localStartDate)) {
                numberOfDays++;
            }
            localStartDate = localStartDate.plusDays(1);
        }
        return (numberOfDays);
    }

}
