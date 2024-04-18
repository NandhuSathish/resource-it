package com.innovature.resourceit.entity.dto.response;

import com.innovature.resourceit.entity.dto.responsedto.PagedResponseDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

class PagedResponseDTOTest {

    @Test
    void testConstructorAndGetters() {
        // Create sample data
        List<String> items = new ArrayList<>();
        items.add("Item1");
        items.add("Item2");
        items.add("Item3");

        int totalPages = 2;
        long totalItems = 3;
        int currentPage = 1;

        // Create PagedResponseDTO instance using the constructor
        PagedResponseDTO<String> pagedResponseDTO = new PagedResponseDTO<>(items, totalPages, totalItems, currentPage);

        // Verify the values using getters
        assertEquals(items, pagedResponseDTO.getItems());
        assertEquals(totalPages, pagedResponseDTO.getTotalPages());
        assertEquals(totalItems, pagedResponseDTO.getTotalItems());
        assertEquals(currentPage, pagedResponseDTO.getCurrentPage());
    }

    @Test
    void testDefaultConstructor() {
        // Create PagedResponseDTO instance using the default constructor
        PagedResponseDTO<String> pagedResponseDTO = new PagedResponseDTO<>();

        // Verify that the default constructor initializes fields to null or default values
        assertNull(pagedResponseDTO.getItems());
        assertEquals(0, pagedResponseDTO.getTotalPages());
        assertEquals(0, pagedResponseDTO.getTotalItems());
        assertEquals(0, pagedResponseDTO.getCurrentPage());
    }

    @Test
    void testSetterMethods() {
        // Create an empty PagedResponseDTO instance
        PagedResponseDTO<String> pagedResponseDTO = new PagedResponseDTO<>();

        // Set values using setter methods
        List<String> items = new ArrayList<>();
        items.add("New Item1");
        items.add("New Item2");
        pagedResponseDTO.setItems(items);

        int totalPages = 3;
        long totalItems = 5;
        int currentPage=1;
        pagedResponseDTO.setTotalPages(totalPages);
        pagedResponseDTO.setTotalItems(totalItems);
        pagedResponseDTO.setCurrentPage(1);
        // Verify the values using getters
        assertEquals(items, pagedResponseDTO.getItems());
        assertEquals(totalPages, pagedResponseDTO.getTotalPages());
        assertEquals(totalItems, pagedResponseDTO.getTotalItems());
        assertEquals(currentPage, pagedResponseDTO.getCurrentPage());
    }
}

