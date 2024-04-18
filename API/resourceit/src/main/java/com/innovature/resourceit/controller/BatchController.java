package com.innovature.resourceit.controller;

import com.innovature.resourceit.service.BatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/")
public class BatchController {

    @Autowired
    private BatchService batchService;

    @PostMapping("allocation-status-expiry")
    public ResponseEntity<Object> updateAllocationStatusExpiryAndTeamSize() {
        batchService.updateAllocationStatusExpiryAndTeamSize();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("reminder")
    public ResponseEntity<Object> sendReminder() {
        batchService.sendReminder();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
