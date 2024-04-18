/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.innovature.resourceit.controller.resourcefilecontroller;

import com.innovature.resourceit.controller.ResourceFileController;
import com.innovature.resourceit.entity.dto.responsedto.SuccessResponseDTO;
import com.innovature.resourceit.exceptionhandler.BadRequestException;
import com.innovature.resourceit.service.ResourceFileUploadService;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author abdul.fahad
 */
@SpringBootTest
@ContextConfiguration(classes = ResourceUploadTest.class)
public class ResourceUploadTest {

    @Mock
    MessageSource messageSource;

    @Mock
    private ResourceFileUploadService resourceFileUploadService;

    @InjectMocks
    ResourceFileController resourceFileController;

    private MultipartFile file;
    private MultipartFile file1;

    @BeforeEach
    public void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);


        file = new MockMultipartFile("file",
                "test.xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                "some excel content".getBytes());
        file1 = new MockMultipartFile("file",
                null,
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                "some excel content".getBytes());
    }

    @Test
    public void resourceUploadValid() throws Exception {
        ResponseEntity<Object> responseEntity = resourceFileController.uploadResourceData(file);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        verify(resourceFileUploadService, times(1)).resourceExcelUpload(file);
    }
    @Test
    public void resourceUploadWithFileNameNull() throws Exception {


        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            resourceFileController.uploadResourceData(file1);
        });
    }

    @Test
    public void resourceUploadInValid() throws Exception {

        when(messageSource.getMessage(eq("FILE_REQUIRED"), eq(null), any()))
                .thenReturn("2000-File required");

        MockMultipartFile emptyFile = new MockMultipartFile(
                "file",
                "",
                "text/plain",
                new byte[0]
        );

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            resourceFileController.uploadResourceData(emptyFile);
        });

        assertNotNull(exception);
        assertEquals("2000-File required", exception.getBody().getDetail());

    }
}
