/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.innovature.resourceit.service.skill;

import com.innovature.resourceit.entity.Skill;
import com.innovature.resourceit.repository.SkillRepository;
import com.innovature.resourceit.service.impli.SkillServiceImpli;
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

/**
 *
 * @author abdul.fahad
 */

@SpringBootTest
@ContextConfiguration(classes = SkillServiceImpliTest.class)
public class SkillServiceImpliTest {
    
    @Mock
    SkillRepository skillRepository;
    
    @InjectMocks
    SkillServiceImpli skillServiceImpli;
    
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    public void getAllSkillsTest(){
        Skill skill1 =new Skill(1, "Java");
        Skill skill2 =new Skill(2, "Nodejs");
        
        List<Skill> skills = Arrays.asList(skill1,skill2);
        
        when(skillRepository.findAll()).thenReturn(skills);
        
        List<Skill> result = skillServiceImpli.getAllSkills();
        
        Assert.assertEquals(skills.size(), result.size());
    }
    
}
