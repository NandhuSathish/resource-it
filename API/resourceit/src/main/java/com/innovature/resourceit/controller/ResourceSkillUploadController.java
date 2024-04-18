package com.innovature.resourceit.controller;

import com.innovature.resourceit.entity.dto.responsedto.SuccessResponseDTO;
import com.innovature.resourceit.exceptionhandler.BadRequestException;
import com.innovature.resourceit.service.SkillProficiencyFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Locale;

@RestController
@RequestMapping("/api/v1/skill/proficiency")
public class ResourceSkillUploadController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceSkillUploadController.class);

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private SkillProficiencyFileUpload skillProficiencyFileUpload;
    @PostMapping("/upload")
    public ResponseEntity<Object> uploadResourceData(@RequestParam("file") MultipartFile file) throws Exception {
        if (!file.isEmpty() && file.getOriginalFilename() != null) {
            if (file.getOriginalFilename() == null || !file.getOriginalFilename().endsWith(".xlsx")) {
                LOGGER.error("Incorrect file");
                throw new BadRequestException(messageSource.getMessage("INCORRECT_FILE", null, Locale.ENGLISH));
            }

            skillProficiencyFileUpload.skillProficiencyFileUpload(file);
        } else {
            LOGGER.error("No file uploaded");
            throw new BadRequestException(messageSource.getMessage("FILE_REQUIRED", null, Locale.ENGLISH));
        }
        return new ResponseEntity<>(new SuccessResponseDTO("200", "Successfully uploaded"), HttpStatus.OK);
    }
}
