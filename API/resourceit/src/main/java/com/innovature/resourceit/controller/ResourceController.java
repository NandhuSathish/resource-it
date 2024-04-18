package com.innovature.resourceit.controller;

import com.innovature.resourceit.entity.dto.requestdto.ResourceRequestDTO;
import com.innovature.resourceit.entity.dto.responsedto.*;
import com.innovature.resourceit.entity.dto.requestdto.ResourceFilterRequestDTO;
import com.innovature.resourceit.exceptionhandler.UserNotFoundException;
import com.innovature.resourceit.service.ResourceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/resource")
public class ResourceController {

    @Autowired
    private ResourceService resourceService;

    @PostMapping("/list-resources")
    public ResponseEntity<Object> getResources(@RequestBody ResourceFilterRequestDTO requestDTO) {
        PagedResponseDTO<ResourceListingResponseDTO> responseDTOs = resourceService.getResources(requestDTO);
        return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getResourceById(@PathVariable Integer id) {
        ResourceResponseDTO resource = resourceService.getResourceById(id);
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Object> add(@Valid @RequestBody ResourceRequestDTO dto) {
        resourceService.add(dto);
        return new ResponseEntity<>(new SuccessResponseDTO("201", "Resource Successfully created"), HttpStatus.CREATED);
    }

    @PutMapping("/{resource_id}")
    public ResponseEntity<Object> updateResource(@Valid @RequestBody ResourceRequestDTO dto,
                                                 @PathVariable("resource_id") Integer resourceId) {

        resourceService.updateResource(resourceId, dto);
        return new ResponseEntity<>(new SuccessResponseDTO("200", "Resource Updated"), HttpStatus.OK);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") Integer id) throws UserNotFoundException {
        resourceService.deleteResource(id);
        return new ResponseEntity<>(new SuccessResponseDTO("200", "Resource Successfully Deleted"), HttpStatus.OK);
    }

    @GetMapping("/managers")
    public ResponseEntity<Object> getManagers() {
        List<ManagerListResponseDTO> resourceList = resourceService.getManagers();
        return new ResponseEntity<>(resourceList, HttpStatus.OK);

    }

}
