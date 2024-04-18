package com.innovature.resourceit.controller;


import com.innovature.resourceit.service.BatchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class BatchControllerTest {

    @Mock
    BatchService batchService;

    @InjectMocks
    BatchController batchController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testUpdateAllocationStatusExpiryAndTeamSize() {
        ResponseEntity<Object> responseEntity =  batchController.updateAllocationStatusExpiryAndTeamSize();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(batchService,times(1)).updateAllocationStatusExpiryAndTeamSize();
    }

    @Test
    public void testSendReminder() {
        ResponseEntity<Object> responseEntity =  batchController.sendReminder();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(batchService,times(1)).sendReminder();
    }
}
