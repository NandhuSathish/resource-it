package com.innovature.resourceit.service.impli;

import com.innovature.resourceit.entity.Resource;
import com.innovature.resourceit.entity.ResourceSkill;
import com.innovature.resourceit.entity.Skill;
import com.innovature.resourceit.exceptionhandler.BadRequestException;
import com.innovature.resourceit.repository.ResourceRepository;
import com.innovature.resourceit.repository.ResourceSkillRepository;
import com.innovature.resourceit.repository.RoleRepository;
import com.innovature.resourceit.repository.SkillRepository;
import com.innovature.resourceit.service.SkillProficiencyFileUpload;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

@Service
public class SkillProficiencyFileUploadImpli implements SkillProficiencyFileUpload {


    private static final String SHEET = "Sheet1";

    private static final Logger LOGGER = LoggerFactory.getLogger(SkillProficiencyFileUploadImpli.class);
    private static final String EMAIL = "email";
    private static final String INTERNAL_SERVER_ERROR = "INTERNAL_SERVER_ERROR";
    private static final String EMAIL_REGEX = "^[a-zA-Z\\d_.+-]+@[a-zA-Z\\d-]+\\.[a-zA-Z\\.]{1,16}$";
    private final List<String> headers = Arrays.asList("Email Id", "Skill", "Proficiency", "Experience");
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private ResourceRepository resourceRepository;
    @Autowired
    private ResourceSkillRepository resourceSkillRepository;
    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private ResourceFileUploadServiceImpli resourceFileUploadServiceImpli;

    @Override
    @Transactional
    public String skillProficiencyFileUpload(MultipartFile file) throws IOException {

        Workbook workbook = null;
        try {
            workbook = new XSSFWorkbook(file.getInputStream());
        } catch (Exception e) {
            LOGGER.error("Incorrect file");
            throw new BadRequestException(messageSource.getMessage("INCORRECT_FILE", null, Locale.ENGLISH));
        }
        Sheet sheet = workbook.getSheet(SHEET);
        Iterator<Row> rows = sheet.iterator();
        resourceFileUploadServiceImpli.fileMaxSizeChecking(file);
        int rowNumber = 0;
        while (rows.hasNext()) {
            Row currentRow = rows.next();

            if (rowNumber == 0) {
                checkingSkillProficiencyHeaders(currentRow);
                rowNumber++;
                continue;
            }
            if (currentRow.getPhysicalNumberOfCells() != 4) {
                workbook.close();
                throw new BadRequestException(messageSource.getMessage("CONTENT_MISMATCH", null, Locale.ENGLISH));
            }

            Iterator<Cell> cellsInRow = currentRow.iterator();


            Resource resource;
            Skill skill;
            while (cellsInRow.hasNext()) {

                Cell emailSheet = cellsInRow.next();
                Cell skillName = cellsInRow.next();
                Cell proficiencyName = cellsInRow.next();
                Cell experienceSheet = cellsInRow.next();
                Double experience;
                Integer experienceInMonths;
                Integer calculatedExperienceInYears;
                Integer calculatedExperienceInMonths;

                if (experienceSheet.getCellType() == CellType.NUMERIC) {
                    // If the cell contains a numeric value
                    double numericValue = experienceSheet.getNumericCellValue();
                    // Convert the numeric value to a Double
                    experience = Double.valueOf(numericValue);
                    calculatedExperienceInMonths = (int) (experience % 10);
                    calculatedExperienceInYears = (int) (experience / 10);
                    experienceInMonths = (int) (calculatedExperienceInYears * 12 + calculatedExperienceInMonths);
                } else {
                    // Handle other types of cells (e.g., string, formula, etc.)
                    throw new BadRequestException(messageSource.getMessage("NUMBER_FORMAT_YEAR", null, Locale.ENGLISH));
                }
                String email = emailSwitchCase(emailSheet);

                Byte proficiencyId = switch (proficiencyName.toString()) {
                    case "Beginner" -> 0;
                    case "Intermediate" -> 1;
                    case "Expert" -> 2;
                    default ->
                            throw new BadRequestException(messageSource.getMessage(INTERNAL_SERVER_ERROR, null, Locale.ENGLISH));
                };
                resource = resourceRepository.findByEmailIgnoreCase(email).orElseThrow(() -> new BadRequestException(messageSource.getMessage("INVALID_EMAIL", null, Locale.ENGLISH)));
                skill = skillRepository.findByNameIgnoreCase(skillName.toString());
                saveSkillProficiency(resource, skill, proficiencyId, experienceInMonths);
            }
        }

        LOGGER.info("File uploaded successfully");
        workbook.close();
        return "success";
    }

    public void saveSkillProficiency(Resource resource, Skill skill, Byte proficiencyId, Integer experienceInMonths) {
        ResourceSkill resourceSkillObj = new ResourceSkill();
        if (skill != null) {
            Optional<ResourceSkill> resourceSkill = resourceSkillRepository.findByResourceIdAndSkillId(resource.getId(), skill.getId());
            if (resourceSkill.isPresent()) {
                resourceSkill.get().setProficiency(proficiencyId);
                resourceSkill.get().setExperience(experienceInMonths);
                resourceSkillRepository.save(resourceSkill.get());

            } else {
                resourceSkillObj.setResource(resource);
                resourceSkillObj.setSkill(skill);
                resourceSkillObj.setProficiency(proficiencyId);
                resourceSkillObj.setExperience(experienceInMonths);
                resourceSkillRepository.save(resourceSkillObj);
            }

        }
    }

    private void checkingSkillProficiencyHeaders(Row currentRow) {
        for (Cell currentCell : currentRow) {
            if (!headers.contains(currentCell.getStringCellValue())) {
                LOGGER.error("Invalid file headers");
                throw new BadRequestException(messageSource.getMessage("INVALID_HEADER_NAME", null, Locale.ENGLISH));
            }
        }
    }

    public String emailSwitchCase(Cell currentCell) {
        String email = resourceFileUploadServiceImpli.isString(EMAIL, currentCell);
        resourceFileUploadServiceImpli.isNullChecking(EMAIL, email);
        if (!Pattern.matches(EMAIL_REGEX, email)) {
            throw new BadRequestException(messageSource.getMessage("INVALID_EMAIL", null, Locale.ENGLISH));
        }
        return email.toLowerCase();
    }


}
