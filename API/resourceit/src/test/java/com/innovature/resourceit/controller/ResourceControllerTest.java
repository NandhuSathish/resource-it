package com.innovature.resourceit.controller;

import com.innovature.resourceit.entity.dto.requestdto.ResourceRequestDTO;
import com.innovature.resourceit.entity.dto.responsedto.*;
import com.innovature.resourceit.entity.dto.requestdto.ResourceFilterRequestDTO;
import com.innovature.resourceit.exceptionhandler.UserNotFoundException;
import com.innovature.resourceit.service.ResourceService;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ContextConfiguration(classes = ResourceControllerTest.class)

public class ResourceControllerTest {

    @InjectMocks
    private ResourceController resourceController;

    @Mock
    private ResourceService resourceService;

    @Test
    void testGetResources() {
        ResourceFilterRequestDTO requestDTO = new ResourceFilterRequestDTO();

        ResourceFilterRequestDTO responseDTOs = new ResourceFilterRequestDTO();
        PagedResponseDTO<ResourceListingResponseDTO> pagedResponseDTO = new PagedResponseDTO<>();
        ResourceListingResponseDTO resourceListingResponseDTO = new ResourceListingResponseDTO();
        resourceListingResponseDTO.setId(1);
        List<ResourceListingResponseDTO> listingResponseDTOs = new ArrayList<>();
        pagedResponseDTO.setCurrentPage(1);
        pagedResponseDTO.setItems(listingResponseDTOs);
        pagedResponseDTO.setTotalItems(5);
        pagedResponseDTO.setTotalPages(1);
        when(resourceService.getResources(requestDTO)).thenReturn(pagedResponseDTO);

        ResponseEntity<Object> responseEntity = resourceController.getResources(requestDTO);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(pagedResponseDTO, responseEntity.getBody());

        verify(resourceService).getResources(eq(requestDTO));
    }

    @Test
    void testGetResourceById() {
        when(resourceService.getResourceById(1)).thenReturn(new ResourceResponseDTO());
        ResponseEntity<Object> responseEntity = resourceController.getResourceById(1);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void testAddResource() {
        ResourceRequestDTO resourceRequestDTO = new ResourceRequestDTO();
        doNothing().when(resourceService).add(resourceRequestDTO);
        ResponseEntity<Object> responseEntity = resourceController.add(resourceRequestDTO);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @Test
    void testUpdateResource() {
        ResourceRequestDTO resourceRequestDTO = new ResourceRequestDTO();
        doNothing().when(resourceService).updateResource(1, resourceRequestDTO);
        ResponseEntity<Object> responseEntity = resourceController.updateResource(resourceRequestDTO, 1);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void testDeleteResource() throws UserNotFoundException {
        Integer resourceId = 1;

        // Mock the behavior of resourceService.deleteResource
        doNothing().when(resourceService).deleteResource(resourceId);

        // Call the method to be tested
        ResponseEntity<Object> responseEntity = resourceController.delete(resourceId);

        // Assertions
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        // Add more assertions as needed

        // Verify that resourceService.deleteResource is called with the correct
        // argument
        verify(resourceService).deleteResource(eq(resourceId));
    }

    @Test
    void testGetManagers() throws UserNotFoundException {
        List<ManagerListResponseDTO> managerList = new ArrayList<>();
        when(resourceService.getManagers()).thenReturn(managerList);

        ResponseEntity<Object> responseEntity = resourceController.getManagers();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(managerList, responseEntity.getBody());

        verify(resourceService).getManagers();
    }

}
