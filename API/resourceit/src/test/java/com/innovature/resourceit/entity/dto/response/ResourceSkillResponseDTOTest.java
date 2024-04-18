package com.innovature.resourceit.entity.dto.response;

import com.innovature.resourceit.entity.ResourceSkill;
import com.innovature.resourceit.entity.Skill;
import com.innovature.resourceit.entity.dto.responsedto.ResourceSkillResponseDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ResourceSkillResponseDTOTest {

    @Test
    void testResourceSkillResponseDTOConstructor() {
        Skill skill = mock(Skill.class);
        when(skill.getId()).thenReturn(1);
        when(skill.getName()).thenReturn("Java");

        ResourceSkill resourceSkill = mock(ResourceSkill.class);
        when(resourceSkill.getSkill()).thenReturn(skill);
        when(resourceSkill.getExperience()).thenReturn(5);

        ResourceSkillResponseDTO responseDTO = new ResourceSkillResponseDTO(resourceSkill);

        assertEquals(1, responseDTO.getSkillId());
        assertEquals("Java", responseDTO.getSkillName());
        assertEquals(5, responseDTO.getExperience());
    }
    @Test
    void testGettersAndSetters() {
        ResourceSkillResponseDTO responseDTO = new ResourceSkillResponseDTO();

        // Set values using setters
        responseDTO.setSkillId(1);
        responseDTO.setSkillName("Java");
        responseDTO.setExperience(5);

        // Verify values using getters
        assertEquals(1, responseDTO.getSkillId());
        assertEquals("Java", responseDTO.getSkillName());
        assertEquals(5, responseDTO.getExperience());
    }
}