package com.innovature.resourceit.entity.dto.response;

import com.innovature.resourceit.entity.dto.responsedto.ResourceListResponseDTO;
import com.innovature.resourceit.entity.dto.responsedto.ResourceListingResponseDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

class ResourceListResponseDTOTest {

    @Test
    void testGettersAndSetters() {
        ResourceListResponseDTO listResponseDTO = new ResourceListResponseDTO();

        // Set values
        listResponseDTO.setCurrentPage(1);
        listResponseDTO.setTotalPages(5);
        listResponseDTO.setTotalElements(50);

        // Create a list of ResourceListingResponseDTO for testing
        List<ResourceListingResponseDTO> resourceDTOList = Arrays.asList(
                createResourceListingResponseDTO(1),
                createResourceListingResponseDTO(2),
                createResourceListingResponseDTO(3)
        );
        listResponseDTO.setResourceListingResponseDTOs(resourceDTOList);

        // Verify values using getters
        assertEquals(1, listResponseDTO.getCurrentPage());
        assertEquals(5, listResponseDTO.getTotalPages());
        assertEquals(50, listResponseDTO.getTotalElements());

        // Verify the list of ResourceListingResponseDTO
        assertEquals(3, listResponseDTO.getResourceListingResponseDTOs().size());
        assertEquals(1, listResponseDTO.getResourceListingResponseDTOs().get(0).getId());
        assertEquals(2, listResponseDTO.getResourceListingResponseDTOs().get(1).getId());
        assertEquals(3, listResponseDTO.getResourceListingResponseDTOs().get(2).getId());
    }

    private ResourceListingResponseDTO createResourceListingResponseDTO(int id) {
        ResourceListingResponseDTO responseDTO = new ResourceListingResponseDTO();
        responseDTO.setId(id);
        return responseDTO;
    }


}
