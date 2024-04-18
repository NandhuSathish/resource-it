/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.innovature.resourceit.controller;

import com.innovature.resourceit.entity.dto.requestdto.ResourceDownloadFilterRequestDTO;
import com.innovature.resourceit.entity.dto.responsedto.SuccessResponseDTO;
import com.innovature.resourceit.exceptionhandler.BadRequestException;
import com.innovature.resourceit.service.ResourceFileDownloadService;
import com.innovature.resourceit.service.ResourceFileUploadService;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Locale;

/**
 * @author abdul.fahad
 */
@RestController
@RequestMapping("/api/v1/resource")
public class ResourceFileController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceFileController.class);

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private ResourceFileUploadService resourceFileUploadService;

    @Autowired
    private ResourceFileDownloadService resourceFileDownloadService;

    @PostMapping("/upload")
    public ResponseEntity<Object> uploadResourceData(@RequestParam("file") MultipartFile file) throws Exception {
        if (!file.isEmpty() && file.getOriginalFilename() != null) {
            if (file.getOriginalFilename() == null || !file.getOriginalFilename().endsWith(".xlsx")) {
                LOGGER.error("Incorrect file");
                throw new BadRequestException(messageSource.getMessage("INCORRECT_FILE", null, Locale.ENGLISH));
            }
            resourceFileUploadService.resourceExcelUpload(file);
        } else {
            LOGGER.error("No file uploaded");
            throw new BadRequestException(messageSource.getMessage("FILE_REQUIRED", null, Locale.ENGLISH));
        }
        return new ResponseEntity<>(new SuccessResponseDTO("200", "Successfully uploaded"), HttpStatus.OK);
    }

    @PostMapping("/download")
    public ResponseEntity<Object> downloadResourceData(HttpServletResponse response, @RequestBody ResourceDownloadFilterRequestDTO requestDTO) {
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"resource.xlsx\"");
        resourceFileDownloadService.resourceExcelDownload(response, requestDTO);

        return new ResponseEntity<>(new SuccessResponseDTO("200", "Successfully downloaded"), HttpStatus.OK);
    }

}
