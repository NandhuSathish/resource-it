package com.innovature.resourceit.entity.dto.response;

import com.innovature.resourceit.entity.dto.responsedto.ManagerListResponseDTO;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ManagerListResponseDTOTest {

    @Test
    void testGettersAndSetters() {
        ManagerListResponseDTO managerDTO = new ManagerListResponseDTO();

        managerDTO.setId(1);
        managerDTO.setName("John Doe");

        assertThat(managerDTO.getId()).isEqualTo(1);
        assertThat(managerDTO.getName()).isEqualTo("John Doe");
    }

}
