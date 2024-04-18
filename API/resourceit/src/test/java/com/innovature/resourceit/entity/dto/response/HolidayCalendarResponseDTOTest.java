package com.innovature.resourceit.entity.dto.response;


import com.innovature.resourceit.entity.dto.responsedto.HolidayCalendarResponseDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HolidayCalendarResponseDTOTest {

    @Test
    void testGettersAndSetters() {
        HolidayCalendarResponseDTO responseDTO = new HolidayCalendarResponseDTO();

        responseDTO.setId(1);
        responseDTO.setYear(2023);
        responseDTO.setUploadedDate("2023-11-17");

        assertEquals(1, responseDTO.getId());
        assertEquals(2023, responseDTO.getYear());
        assertEquals("2023-11-17", responseDTO.getUploadedDate());
    }
}
