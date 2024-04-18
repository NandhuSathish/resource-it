package com.innovature.resourceit.util;

import com.innovature.resourceit.entity.*;
import com.innovature.resourceit.repository.*;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class EmailUtils {
    @Autowired
    ResourceRepository resourceRepository;
    @Autowired
    ResourceSkillWiseAllocationRequestRepository skillWiseAllocationRequestRepository;
    @Autowired
    ResourceAllocationRequestRepository resourceAllocationRequestRepository;
    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    DepartmentRepository departmentRepository;
    @Autowired
    SkillRepository skillRepository;
    @Autowired
    private JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String sendEmail;
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailUtils.class);
    private static final String REQUESTED_BY = "]<br>Requested By : ";
    private static final String DEPARTMENT = "]<br>Department : ";

    private static final String ALLOCATION_PERIOD = " <br> Allocation Period : ";
    private static final String EXPERIENCE = "<br>Experience : ";

    private static final String COUNT = "<br>Count : ";
    private static final String SKILL_REQUIRED = "<br>Skills Required : ";
    private static final String ALLOCATION_APPROVED = "Your allocation request has been approved. <br>Project : ";
    private static final String RESOURCE_NAME = "]<br>Resource Name : ";
    private static final String PROJECT_TYPE = " <br> Project Type    : ";
    private static final String ESTIMATED_MAN_DAYS = " <br> Estimated Man Days : ";

    private static final String CLIENT = " <br> Client          : ";
    private static final String PROJECT_PERIOD = " <br> Project Period  : ";
    private static final String TECHNOLOGIES = "<br>Technologies  :<br>";
    private static final String FORMATTER = "&#8226; ";
    private static final String BILLABLE = "Customer Billing";
    private static final String INTERNAL = "Innovature Billing";
    private static final String PROJECT = "<br><br>Project : ";
    private static final String PROJECT_MANAGER = " <br> Project Manager : ";
    private static final String SIGNATURE = """
            <table style="color:rgb(0,0,0);font-family:&quot;Times New Roman&quot;;font-size:medium;width:483px;" cellspacing="0" cellpadding="0" border="0">
              <tbody>

                <tr>
                  <td style="font-size:10pt;font-family:Arial;width:183px;" valign="middle"><a href="http://innovature.ai/" target="_blank"><img src="https://innovature.ai/wp-content/uploads/2020/03/innovature-logo.png" alt="innovaturelabs" class="CToWUd" border="0" style="height:65px;"></a></td>
                  <td style="font-size:10pt;font-family:Arial;width:400px;padding-left:20px;border-left:1px solid rgb(20,95,177)" valign="top">
                    <strong>t:</strong><span style="padding-left:11px">+91-484-4038120</span><br><strong>w:</strong>&nbsp;<a href="https://innovature.ai/" style="color:rgb(22,141,203);text-decoration-line:none" target="_blank"><span style="padding-left:4px">https://innovature.ai</span></a>
                    <div style="padding-top:3px"><a href="https://www.facebook.com/innovature/" target="_blank"><img src="https://innovature.ai/wp-content/uploads/2020/06/facebook.png" class="CToWUd" border="0" style="height:16px;"></a>&nbsp;&nbsp;<a href="https://in.linkedin.com/company/innovature-labs" target="_blank"><img src="https://innovature.ai/wp-content/uploads/2020/06/linkdin.png" class="CToWUd" border="0" style="height:16px;"></a>&nbsp;&nbsp;<a href="https://twitter.com/Innovature_ai" target="_blank"><img src="https://innovature.ai/wp-content/uploads/2020/06/twitter-1.png" class="CToWUd" border="0" style="height:16px;"></a>&nbsp;&nbsp;<a href="https://www.google.com/maps/place/Innovature+Labs/@10.0039836,76.3748932,19.25z/data=!4m5!3m4!1s0x3b080c5cfabc4eb7:0xee6880f8c72caaf3!8m2!3d10.0040958!4d76.3756261" target="_blank"><img src="
            https://innovature.ai/wp-content/uploads/2017/07/map-pin.png" class="CToWUd" border="0" style="height:16px;"></a>&nbsp;</div>
                  </td>
                  <td></td>
                  <td style="font-size:10pt;font-family:Arial;width:400px; padding-left: 20px; padding-bottom:10px;" valign="middle">

                    <img src="https://drive.google.com/uc?export=view&id=1YnIUnu9hXulNHGa1jAZqRom62YV3Qsok" alt="innovaturelabs" class="CToWUd" border="0" style=" width:475px;">

                </td>

                </tr>
              </tbody>
            </table>
            <span style="color:#666666;font-size:x-small"><i>Disclaimer:- The information contained in this electronic message and any attachments to this message are intended for the exclusive use of the addressee(s) and may contain proprietary, confidential or privileged information. If you are not the intended recipient, you should not disseminate, distribute or copy this email. Please notify the sender immediately and destroy all copies of this message and any attachments. The views expressed in this email message (including the enclosure/(s) or attachment/(s) if any) are those of the individual sender, except where the sender expressly, and with authority, states them to be the views of Innovature. Before opening any mail and attachments please check them for viruses. Innovature does not accept any liability for virus-infected emails.</i><br></span>
            """;

    // for allocation
    @Async
    @Transactional
    public void allocationMailNotification(List<Integer> userList, Integer type,
                                           Optional<ResourceSkillWiseAllocationRequest> skillWiseReqObj,
                                           List<ResourceAllocationRequest> resourceWiseReqList) {
        String subject = "";
        StringBuilder message = new StringBuilder();
        Resource manager = null;
        String startDate = null;
        String endDate = null;
        Project project = null;
        Resource requestedBy = null;
        ResourceSkillWiseAllocationRequest skillWiseRequest = null;
        Set<SkillExperience> requestSkillsList;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        StringBuilder skills = new StringBuilder();
        // Assuming there is only one project for all requests in the list
        if (!resourceWiseReqList.isEmpty()) {
            ResourceAllocationRequest firstRequest = resourceWiseReqList.get(0);
            Optional<Project> projectOpt = projectRepository.findById(firstRequest.getProject().getProjectId());
            project = projectOpt.orElse(null);
            assert project != null;
            Optional<Resource> managerOpt = resourceRepository.findByIdAndStatus(project.getManager().getId(),
                    Resource.Status.ACTIVE.value);
            manager = managerOpt.orElse(null);
            startDate = formatter.format(firstRequest.getStartDate());
            endDate = formatter.format(firstRequest.getEndDate());
            Optional<Resource> requestedByOpt = resourceRepository
                    .findByIdAndStatus(firstRequest.getRequestedBy().getId(), Resource.Status.ACTIVE.value);
            requestedBy = requestedByOpt.orElse(null);

        } else if (skillWiseReqObj.isPresent()) {
            skillWiseRequest = skillWiseReqObj.get();
            Optional<Project> projectOpt = projectRepository.findById(skillWiseRequest.getProject().getProjectId());
            project = projectOpt.orElse(null);
            assert project != null;
            Optional<Resource> managerOpt = resourceRepository.findByIdAndStatus(project.getManager().getId(),
                    Resource.Status.ACTIVE.value);
            manager = managerOpt.orElse(null);
            startDate = formatter.format(skillWiseRequest.getStartDate());
            endDate = formatter.format(skillWiseRequest.getEndDate());
            requestSkillsList = skillWiseRequest.getSkillExperiences();
            Optional<Resource> requestedByOpt = resourceRepository
                    .findByIdAndStatus(skillWiseRequest.getRequestedBy().getId(), Resource.Status.ACTIVE.value);
            requestedBy = requestedByOpt.orElse(null);
            for (SkillExperience requestSkills : requestSkillsList) {
                String proficiency = getProficiency(requestSkills.getProficiency());
                skills.append(" ").append(requestSkills.getName()).append(" : ")
                        .append(requestSkills.getSkillMinValue()).append("-")
                        .append(requestSkills.getSkillMaxValue()).append(" Y :")
                        .append(proficiency).append("<br> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
            }
            if (skills.length() > 0) {
                skills = new StringBuilder(skills.substring(1));
            }
        }
        switch (type) {
            case 1 -> {
                assert project != null;
                assert manager != null;
                subject = "Resource-Wise Allocation Request for " + project.getName() + "["
                        + project.getProjectCode() + "]";
                // Iterate through the list of ResourceAllocationRequest to construct the
                // message
                assert requestedBy != null;
                message.append(
                                "A  request for allocation has been received.Please find the details below. <br>Project : ")
                        .append(project.getName()).append("[").append(project.getProjectCode()).append(REQUESTED_BY)
                        .append(requestedBy.getName()).append(ALLOCATION_PERIOD).append(startDate).append(" - ")
                        .append(endDate).append("<br>");
                for (ResourceAllocationRequest resourceWiseRequest : resourceWiseReqList) {
                    Optional<Resource> resourceOptional = resourceRepository
                            .findByIdAndStatus(resourceWiseRequest.getResource().getId(), Resource.Status.ACTIVE.value);
                    Resource resource = resourceOptional.orElse(null);
                    assert resource != null;
                    message.append("<br>Resource Name : ").append(resource.getName()).append("[")
                            .append(resource.getEmployeeId()).append(DEPARTMENT)
                            .append(resource.getDepartment().getName()).append("<br>");
                }
            }
            case 2 -> {
                assert skillWiseRequest != null;
                assert requestedBy != null;
                Department department = skillWiseRequest.getDepartment();
                subject = "Technology-Wise Allocation Request for " + project.getName() + "["
                        + project.getProjectCode() + "]";
                assert manager != null;
                message = new StringBuilder("A  request for allocation has been received. <br>Project : "
                        + project.getName() + "[" + project.getProjectCode()
                        + REQUESTED_BY + requestedBy.getName()
                        + "<br>Department : " + department.getName()
                        + EXPERIENCE + skillWiseRequest.getExperience()
                        + COUNT + skillWiseRequest.getResourceCount()
                        + ALLOCATION_PERIOD + startDate + " - " + endDate
                        + SKILL_REQUIRED + skills);
            }
            case 3 -> {
                assert project != null;
                assert manager != null;
                assert requestedBy != null;
                subject = "Edit Allocation Request for " + project.getName() + "[" + project.getProjectCode()
                        + "]";
                ResourceAllocationRequest resourceWiseRequest = resourceWiseReqList.get(0);
                Optional<Resource> resourceOptional = resourceRepository
                        .findByIdAndStatus(resourceWiseRequest.getResource().getId(), Resource.Status.ACTIVE.value);
                Resource resource = resourceOptional.orElse(null);
                assert resource != null;
                message.append("An  allocation edit request has been received. <br>Project : ")
                        .append(project.getName()).append("[").append(project.getProjectCode()).append(REQUESTED_BY)
                        .append(requestedBy.getName())
                        .append("<br>Resource Name : ").append(resource.getName()).append("[")
                        .append(resource.getEmployeeId()).append(DEPARTMENT).append(resource.getDepartment().getName())
                        .append(ALLOCATION_PERIOD).append(startDate).append(" - ").append(endDate).append("<br>");
            }
            case 4 -> {
                ResourceAllocationRequest resourceWiseRequest = resourceWiseReqList.get(0);
                assert resourceWiseRequest != null;
                assert project != null;
                Optional<Resource> resourceOptional = resourceRepository
                        .findByIdAndStatus(resourceWiseRequest.getResource().getId(), Resource.Status.ACTIVE.value);
                Resource resource = resourceOptional.orElse(null);
                assert resource != null;
                if (resourceWiseRequest.getAllocation() != null) {
                    subject = "Approved Allocation-Edit Request for " + project.getName() + "["
                            + project.getProjectCode() + "]";
                    message.append(ALLOCATION_APPROVED).append(project.getName()).append("[")
                            .append(project.getProjectCode()).append(RESOURCE_NAME).append(resource.getName())
                            .append("[").append(resource.getEmployeeId()).append(DEPARTMENT)
                            .append(resource.getDepartment().getName())
                            .append(ALLOCATION_PERIOD).append(startDate).append(" - ").append(endDate).append("<br>");
                } else {
                    subject = "Approved Resource-Wise Request for " + project.getName() + "["
                            + project.getProjectCode() + "]";
                    message.append(ALLOCATION_APPROVED).append(project.getName()).append("[")
                            .append(project.getProjectCode()).append(RESOURCE_NAME)
                            .append(resourceWiseRequest.getResource().getName()).append("[")
                            .append(resourceWiseRequest.getResource().getEmployeeId()).append(DEPARTMENT)
                            .append(resourceWiseRequest.getResource().getDepartment().getName())
                            .append(ALLOCATION_PERIOD).append(startDate).append(" - ")
                            .append(endDate).append("<br>");
                }
            }
            case 6 -> {
                assert project != null;
                subject = "Allocation Request Rejected " + project.getName() + "[" + project.getProjectCode()
                        + "]";
                ResourceAllocationRequest resourceWiseRequest = resourceWiseReqList.get(0);
                assert resourceWiseRequest != null;
                Optional<Resource> resourceOptional = resourceRepository
                        .findByIdAndStatus(resourceWiseRequest.getResource().getId(), Resource.Status.ACTIVE.value);
                Resource resource = resourceOptional.orElse(null);
                assert resource != null;
                message.append("Your  request for allocation is rejected.<br>Reason for Rejection : "
                                + resourceWiseRequest.getRejectionReason() + "<br>Project :  ").append(project.getName())
                        .append("[").append(project.getProjectCode()).append(RESOURCE_NAME).append(resource.getName())
                        .append("[").append(resource.getEmployeeId()).append(DEPARTMENT)
                        .append(resource.getDepartment().getName())
                        .append(ALLOCATION_PERIOD).append(startDate).append(" - ").append(endDate).append("<br>");
            }
            // technology-wise rejection
            case 7 -> {
                assert skillWiseRequest != null;
                Department departments1 = skillWiseRequest.getDepartment();
                subject = "Allocation Request Rejected " + project.getName() + "[" + project.getProjectCode()
                        + "]";
                message = new StringBuilder(
                        "Your request is for technology-wise allocation rejected.<br>Reason for Rejection : "
                                + skillWiseRequest.getRejectionReason() + "<br>Project :  " + project.getName()
                                + "[" + project.getProjectCode() + DEPARTMENT + departments1.getName()
                                + EXPERIENCE + skillWiseRequest.getExperience()
                                + COUNT + skillWiseRequest.getResourceCount()
                                + ALLOCATION_PERIOD + startDate + " - " + endDate
                                + SKILL_REQUIRED + skills);
            }
            case 18 -> {
                assert skillWiseRequest != null;
                Department departments1 = skillWiseRequest.getDepartment();
                subject = "Allocation Request Approved " + project.getName() + "[" + project.getProjectCode()
                        + "]";
                message = new StringBuilder(
                        "Your allocation request for technology-wise allocation has been approved.<br>Project :  "
                                + project.getName()
                                + "[" + project.getProjectCode() + DEPARTMENT + departments1.getName()
                                + EXPERIENCE + skillWiseRequest.getExperience()
                                + COUNT + skillWiseRequest.getResourceCount()
                                + ALLOCATION_PERIOD + startDate + " - " + endDate
                                + SKILL_REQUIRED + skills);
            }
            default -> LOGGER.error("INVALID TYPE!");
        }

        sendEmailToUsers(userList, subject, String.valueOf(message));
    }

    public String getProficiency(List<Byte> proficiency) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < proficiency.size(); i++) {
            byte level = proficiency.get(i);
            String proficiencyLevel = switch (level) {
                case 0 -> "Beginner";
                case 1 -> "Intermediate";
                case 2 -> "Expert";
                default -> " ";
            };

            result.append(proficiencyLevel);
            if (i < proficiency.size() - 1) {
                result.append(", ");
            }
        }

        return result.toString();
    }

    /// For Project
    @Async
    @Transactional
    public void projectMailNotification(List<Integer> userList, Integer type,
                                        Optional<ProjectRequest> projectRequestOptional,
                                        Optional<Project> projectOptional) {
        String managerName = "";
        Project project = null;
        ProjectRequest projectRequest = null;
        String projectType = "";
        String estimatedManDays = "";

        if (projectOptional.isPresent()) {
            project = projectOptional.get();
            managerName = getManagerName(project);
            projectType = getProjectType(project.getProjectType());
            estimatedManDays = getEstimatedManDays(project.getManDay());
        } else if (projectRequestOptional.isPresent()) {
            projectRequest = projectRequestOptional.get();
            projectType = getProjectType(projectRequest.getProjectType());
            estimatedManDays = getEstimatedManDays(projectRequest.getManDay());
        }

        switch (type) {
            case 8 -> {
                assert project != null;
                approvedProjectCreationRequest(userList, project, managerName, projectType, estimatedManDays);
            }
            case 9 -> {
                assert projectRequest != null;
                receivedProjectCreationRequest(userList, projectRequest, project, managerName, projectType,
                        estimatedManDays);
            }
            case 10 -> {
                assert projectRequest != null;
                rejectedProjectCreationRequest(userList, projectRequest, project, projectType, estimatedManDays);
            }
            case 11 -> {
                assert projectRequest != null;
                receivedProjectEditRequest(userList, projectRequest, projectType, estimatedManDays);
            }
            case 12 -> {
                assert projectRequest != null;
                rejectedProjectEditRequest(userList, projectRequest, projectType, estimatedManDays);
            }
            case 13 -> {
                assert project != null;
                approvedProjectEditRequest(userList, project, projectType, estimatedManDays);
            }
            case 14 -> {
                assert project != null;
                changeInProjectDetails(userList, project, projectType, estimatedManDays);
            }
            case 15 -> {
                assert project != null;
                projectAssignment(userList, project, projectType, estimatedManDays);
            }
            case 16 -> {
                assert project != null;
                changeInProjectAssignment(userList, project);
            }
            case 17 -> {
                assert project != null;
                changeInProjectDetailsHR(userList, project, managerName, projectType, estimatedManDays);
            }
            default -> LOGGER.error("INVALID TYPE!");
        }
    }

    // Methods for each case

    public void approvedProjectCreationRequest(List<Integer> userList, Project project,
                                               String managerName, String projectType, String estimatedManDays) {
        String subject = "Approved Project creation Request for " + project.getName() + "[" +
                project.getProjectCode() + "]";
        StringBuilder message = buildProjectCreationMessageForApproval(
                "Your Project creation request has been approved.",
                project, managerName, projectType, estimatedManDays);
        sendEmailToUsers(userList, subject, String.valueOf(message));
    }

    private void receivedProjectCreationRequest(List<Integer> userList, ProjectRequest projectRequest, Project project,
                                                String managerName, String projectType, String estimatedManDays) {
        String subject = "Received  Project creation Request for " + projectRequest.getName() + "[" +
                projectRequest.getProjectCode() + "]";
        StringBuilder message = buildProjectCreationMessage("A Project creation request has been received.",
                projectRequest,
                project, managerName, projectType, estimatedManDays);
        sendEmailToUsers(userList, subject, String.valueOf(message));
    }

    private void rejectedProjectCreationRequest(List<Integer> userList, ProjectRequest projectRequest, Project project,
                                                String projectType, String estimatedManDays) {
        String subject = "Rejected  Project creation Request for " + projectRequest.getName() + "[" +
                projectRequest.getProjectCode() + "]";
        StringBuilder message = buildProjectCreationMessage(
                "Your project creation request has been rejected.Please review the details.",
                projectRequest, project, "", projectType, estimatedManDays);
        sendEmailToUsers(userList, subject, String.valueOf(message));
    }

    private void receivedProjectEditRequest(List<Integer> userList, ProjectRequest projectRequest, String projectType,
                                            String estimatedManDays) {
        String subject = "Received  Project Edit Request for " + projectRequest.getName() + "[" +
                projectRequest.getProjectCode() + "]";
        Optional<Project> projectOptional = projectRepository.findById(projectRequest.getProject().getProjectId());
        Project project = projectOptional.orElse(null);
        assert project != null;
        StringBuilder message = buildProjectEditMessage("A Project edit request has been received.", projectRequest,
                getManagerName(project), projectType, estimatedManDays);
        sendEmailToUsers(userList, subject, String.valueOf(message));
    }

    private void rejectedProjectEditRequest(List<Integer> userList, ProjectRequest projectRequest, String projectType,
                                            String estimatedManDays) {
        String subject = "Rejected Project Edit Request for " + projectRequest.getName() + "[" +
                projectRequest.getProjectCode() + "]";
        Optional<Project> projectOptional = projectRepository.findById(projectRequest.getProject().getProjectId());
        Project project = projectOptional.orElse(null);
        assert project != null;
        StringBuilder message = buildProjectEditMessage(
                "Project edit request has been rejected.Please review the details.",
                projectRequest, getManagerName(project), projectType, estimatedManDays);
        sendEmailToUsers(userList, subject, String.valueOf(message));
    }

    private void approvedProjectEditRequest(List<Integer> userList, Project project, String projectType,
                                            String estimatedManDays) {
        String subject = "Approved Project Edit Request for " + project.getName() + "[" +
                project.getProjectCode() + "]";
        StringBuilder message = buildProjectEditMessageApproval("Your request for project edit has been approved.",
                project,
                getManagerName(project), projectType, estimatedManDays);
        sendEmailToUsers(userList, subject, String.valueOf(message));
    }

    private void changeInProjectDetails(List<Integer> userList, Project project, String projectType,
                                        String estimatedManDays) {
        String subject = "Change in project details " + project.getName() + "[" +
                project.getProjectCode() + "]";
        StringBuilder message = buildProjectDetailsChangeMessage(
                "Please be informed that the project details for the following project got changed.",
                project, getManagerName(project), projectType, estimatedManDays);
        sendEmailToUsers(userList, subject, String.valueOf(message));
    }

    private void projectAssignment(List<Integer> userList, Project project, String projectType,
                                   String estimatedManDays) {
        String subject = "Project Assignment " + project.getName() + "[" +
                project.getProjectCode() + "]";
        StringBuilder message = buildProjectAssignmentMessage(
                "Congratulations! You have been appointed as the project manager for the following project.",
                project, projectType, estimatedManDays);
        sendEmailToUsers(userList, subject, String.valueOf(message));
    }

    private void changeInProjectAssignment(List<Integer> userList, Project project) {
        String subject = "Change in Project Assignment " + project.getName() + "[" +
                project.getProjectCode() + "]";
        StringBuilder message = buildProjectAssignmentMessage(
                "Please be informed that there is a change in project assignment and you have been removed from the project.",
                project, getProjectType(project.getProjectType()), getEstimatedManDays(project.getManDay()));
        sendEmailToUsers(userList, subject, String.valueOf(message));
    }

    private void changeInProjectDetailsHR(List<Integer> userList, Project project, String managerName,
                                          String projectType, String estimatedManDays) {
        String subject = "Change in project details " + project.getName() + "[" +
                project.getProjectCode() + "]";
        StringBuilder message = buildProjectDetailsChangeMessage(
                "Please be informed that the project details for the following project got changed.",
                project, managerName, projectType, estimatedManDays);
        sendEmailToUsers(userList, subject, String.valueOf(message));
    }

    private StringBuilder buildProjectEditMessage(String intro, ProjectRequest projectRequest, String managerName,
                                                  String projectType, String estimatedManDays) {
        StringBuilder message = new StringBuilder(intro + PROJECT +
                projectRequest.getName() + "[" + projectRequest.getProjectCode() + "]" +
                PROJECT_TYPE + projectType +
                (managerName != null ? PROJECT_MANAGER + managerName : "") +
                ESTIMATED_MAN_DAYS + estimatedManDays +
                CLIENT + projectRequest.getClientName() +
                PROJECT_PERIOD + formatDate(projectRequest.getStartDate()) + " - "
                + formatDate(projectRequest.getEndDate()) + TECHNOLOGIES);

        for (Skill technology : projectRequest.getSkill()) {
            message.append(FORMATTER).append(technology.getName()).append("<br>");
        }

        return message;
    }

    private StringBuilder buildProjectEditMessageApproval(String intro, Project project, String managerName,
                                                          String projectType, String estimatedManDays) {
        StringBuilder message = new StringBuilder(intro + PROJECT +
                project.getName() + "[" + project.getProjectCode() + "]" +
                PROJECT_TYPE + projectType +
                (managerName != null ? PROJECT_MANAGER + managerName : "") +
                ESTIMATED_MAN_DAYS + estimatedManDays +
                CLIENT + project.getClientName() +
                PROJECT_PERIOD + formatDate(project.getStartDate()) + " - " + formatDate(project.getEndDate())
                + TECHNOLOGIES);

        for (Skill technology : project.getSkill()) {
            message.append(FORMATTER).append(technology.getName()).append("<br>");
        }

        return message;
    }

    private StringBuilder buildProjectDetailsChangeMessage(String intro, Project project, String managerName,
                                                           String projectType, String estimatedManDays) {
        StringBuilder message = new StringBuilder(intro + PROJECT +
                project.getName() + "[" + project.getProjectCode() + "]" +
                PROJECT_TYPE + projectType +
                (managerName != null ? " <br> Project Manager    : " + managerName : "") +
                ESTIMATED_MAN_DAYS + estimatedManDays +
                CLIENT + project.getClientName() +
                PROJECT_PERIOD + formatDate(project.getStartDate()) + " - " + formatDate(project.getEndDate())
                + TECHNOLOGIES);

        for (Skill technology : project.getSkill()) {
            message.append(FORMATTER).append(technology.getName()).append("<br>");
        }

        return message;
    }

    private StringBuilder buildProjectAssignmentMessage(String intro, Project project,
                                                        String projectType, String estimatedManDays) {
        StringBuilder message = new StringBuilder(intro + PROJECT +
                project.getName() + "[" + project.getProjectCode() + "]" +
                PROJECT_TYPE + projectType +
                ESTIMATED_MAN_DAYS + estimatedManDays +
                CLIENT + project.getClientName() +
                PROJECT_PERIOD + formatDate(project.getStartDate()) + " - " + formatDate(project.getEndDate())
                + TECHNOLOGIES);

        for (Skill technology : project.getSkill()) {
            message.append(FORMATTER).append(technology.getName()).append("<br>");
        }

        return message;
    }

    private StringBuilder buildProjectCreationMessageForApproval(String intro, Project project,
                                                                 String managerName, String projectType, String estimatedManDays) {
        StringBuilder message = new StringBuilder(
                intro + PROJECT + (project.getName() + "[" + project.getProjectCode() + "]") +
                        PROJECT_TYPE + projectType +
                        ESTIMATED_MAN_DAYS + estimatedManDays +
                        (managerName != null ? PROJECT_MANAGER + managerName : "") +
                        CLIENT + (project.getClientName()) +
                        PROJECT_PERIOD + formatDate(project.getStartDate()) +
                        " - " + formatDate(project.getEndDate()) + TECHNOLOGIES);

        for (Skill technology : project.getSkill()) {
            message.append(FORMATTER).append(technology.getName()).append("<br>");
        }

        return message;
    }

    private StringBuilder buildProjectCreationMessage(String intro, ProjectRequest projectRequest, Project project,
                                                      String managerName, String projectType, String estimatedManDays) {
        StringBuilder message = new StringBuilder(intro + PROJECT
                + (projectRequest != null ? projectRequest.getName() + "[" + projectRequest.getProjectCode() + "]"
                : project.getName() + "[" + project.getProjectCode() + "]")
                +
                PROJECT_TYPE + projectType +
                ESTIMATED_MAN_DAYS + estimatedManDays +
                (managerName != null ? PROJECT_MANAGER + managerName : "") +
                CLIENT + (projectRequest != null ? projectRequest.getClientName() : project.getClientName()) +
                PROJECT_PERIOD
                + formatDate(projectRequest != null ? projectRequest.getStartDate() : project.getStartDate()) +
                " - " + formatDate(projectRequest != null ? projectRequest.getEndDate() : project.getEndDate())
                + TECHNOLOGIES);

        if (projectRequest != null) {
            for (Skill technology : projectRequest.getSkill()) {
                message.append(FORMATTER).append(technology.getName()).append("<br>");
            }
        } else {
            for (Skill technology : project.getSkill()) {
                message.append(FORMATTER).append(technology.getName()).append("<br>");
            }
        }

        return message;
    }

    private String getManagerName(Project project) {
        if (project.getManager() != null) {
            Optional<Resource> managerOpt = resourceRepository.findByIdAndStatus(project.getManager().getId(),
                    Resource.Status.ACTIVE.value);
            Resource manager = managerOpt.orElse(null);
            return manager != null ? manager.getName() : "";
        }
        return "";
    }

    private String getProjectType(Byte projectType) {
        return projectType == 1 ? BILLABLE : INTERNAL;
    }

    private String getEstimatedManDays(Integer manDay) {
        return manDay == null ? "--" : manDay.toString();
    }

    private String formatDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return date != null ? formatter.format(date) : "--";
    }

    private void sendEmailToUsers(List<Integer> userList, String subject, String message) {
        for (int resourceId : userList) {
            Optional<Resource> optResource = resourceRepository.findByIdAndStatus(resourceId,
                    Resource.Status.ACTIVE.value);
            if (optResource.isPresent()) {
                Resource resource = optResource.get();
                String greeting = "Hi " + resource.getName() + ",";
                String toEmail = resource.getEmail();
                if (toEmail != null) {
                    try {
                        MimeMessage message4 = mailSender.createMimeMessage();
                        MimeMessageHelper helper = new MimeMessageHelper(message4);
                        helper.setFrom(sendEmail);
                        helper.setSubject(subject);
                        InternetAddress[] toAddresses = InternetAddress.parse(toEmail);
                        helper.setTo(toAddresses);
                        helper.setText("<form >" + greeting
                                        + "<br>"
                                        + message
                                        + "<br><br><b>Regards,</b>" + "<br><b>Resource Manager</b></form><br>" + SIGNATURE,
                                true);
                        mailSender.send(message4);
                    } catch (MessagingException e) {
                        LOGGER.error("Error in mail sending:", e);
                    }
                }

            }
        }
    }

}
