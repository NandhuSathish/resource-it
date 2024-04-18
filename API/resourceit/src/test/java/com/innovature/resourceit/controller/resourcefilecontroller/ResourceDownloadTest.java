/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.innovature.resourceit.controller.resourcefilecontroller;

import com.innovature.resourceit.controller.ResourceFileController;
import com.innovature.resourceit.entity.dto.requestdto.ResourceDownloadFilterRequestDTO;
import com.innovature.resourceit.entity.dto.responsedto.SuccessResponseDTO;
import com.innovature.resourceit.service.ResourceFileDownloadService;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;

/**
 *
 * @author abdul.fahad
 */
@SpringBootTest
@ContextConfiguration(classes = ResourceDownloadTest.class)
public class ResourceDownloadTest {

    @Mock
    MessageSource messageSource;

    @Mock
    private ResourceFileDownloadService resourceFileDownloadService;
    
    @Mock
    private HttpServletResponse response;

    @InjectMocks
    ResourceFileController resourceFileController;

    @BeforeEach
    public void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    public void resourceDownloadValid() throws Exception {
        doNothing().when(resourceFileDownloadService).resourceExcelDownload(any(HttpServletResponse.class), any(ResourceDownloadFilterRequestDTO.class));

        ResourceDownloadFilterRequestDTO requestDTO = new ResourceDownloadFilterRequestDTO();
        // Call the method to be tested
        ResponseEntity<Object> responseEntity = resourceFileController.downloadResourceData(response, requestDTO);

        // Assertions
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Successfully downloaded", ((SuccessResponseDTO) responseEntity.getBody()).getSuccessMessage());

        // Verify that setContentDisposition is called with the correct argument
        verify(response).setHeader(eq(HttpHeaders.CONTENT_DISPOSITION), eq("attachment; filename=\"resource.xlsx\""));
    }
    
}
