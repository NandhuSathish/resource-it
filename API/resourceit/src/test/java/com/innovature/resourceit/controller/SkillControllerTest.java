package com.innovature.resourceit.controller;

import com.innovature.resourceit.entity.Skill;
import com.innovature.resourceit.service.SkillService;
import java.util.Arrays;
import java.util.List;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = SkillControllerTest.class)
public class SkillControllerTest {
    
    @Mock
    SkillService skillService;
    
    @InjectMocks
    SkillController skillController;
    
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    public void getAllSkillsTest(){
        
        Skill skill1 =new Skill(1, "Java");
        Skill skill2 =new Skill(2, "Nodejs");
        
        List<Skill> skills = Arrays.asList(skill1,skill2);
        
        when(skillService.getAllSkills()).thenReturn(skills);
        
        List<Skill> result = skillController.getAllSkills();
        
        Assert.assertEquals(skills.size(), result.size());
    }
}
