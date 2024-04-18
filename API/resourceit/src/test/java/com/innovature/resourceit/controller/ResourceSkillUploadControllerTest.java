package com.innovature.resourceit.controller;

import com.innovature.resourceit.exceptionhandler.BadRequestException;
import com.innovature.resourceit.service.HolidayCalenderService;
import com.innovature.resourceit.service.SkillProficiencyFileUpload;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
@ContextConfiguration(classes = ResourceSkillUploadControllerTest.class)
public class ResourceSkillUploadControllerTest {
    @Mock
    MessageSource messageSource;

    @Mock
    SkillProficiencyFileUpload skillProficiencyFileUpload;

    @InjectMocks
    ResourceSkillUploadController resourceSkillUploadController;

    @Mock
    HttpServletResponse response;

    @Mock
    PrintWriter writer;

    @BeforeEach
    public void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testResourceSkillUpload() throws Exception {
        String csvData = "Date,Day,Description\nval1,val2,val3\n";
        InputStream is = new ByteArrayInputStream(csvData.getBytes(StandardCharsets.UTF_8));
        MultipartFile mockFile = new MockMultipartFile("data", "filename.xlsx", "text/xlsx", is);

        when(skillProficiencyFileUpload.skillProficiencyFileUpload(mockFile)).thenReturn("success");

        ResponseEntity<Object> response = resourceSkillUploadController.uploadResourceData(mockFile);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    @Test
    void testResourceSkillUploadThrowsBadRequest() throws Exception {
        String csvData = "Date,Day,Description\nval1,val2,val3\n";
        InputStream is = new ByteArrayInputStream(csvData.getBytes(StandardCharsets.UTF_8));
        MultipartFile mockFile = new MockMultipartFile("data", "filename.csv", "text/csv", is);

        when(skillProficiencyFileUpload.skillProficiencyFileUpload(mockFile)).thenReturn("success");
        Assertions.assertThrows(BadRequestException.class, () -> resourceSkillUploadController.uploadResourceData(mockFile));
    }
    @Test
    void testResourceSkillUploadInValid() throws Exception {

        when(messageSource.getMessage(eq("FILE_REQUIRED"), eq(null), any()))
                .thenReturn("2000-File required");
        MockMultipartFile emptyFile = new MockMultipartFile(
                "file",
                "",
                "text/plain",
                new byte[0]
        );

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            resourceSkillUploadController.uploadResourceData(emptyFile);
        });

        assertNotNull(exception);
        assertEquals("2000-File required", exception.getBody().getDetail());
    }

}
