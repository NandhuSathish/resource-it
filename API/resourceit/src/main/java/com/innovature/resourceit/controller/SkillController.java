package com.innovature.resourceit.controller;

import com.innovature.resourceit.entity.Skill;
import com.innovature.resourceit.service.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/skill")
public class SkillController {

    @Autowired
    SkillService skillService;


    @GetMapping
    public List<Skill>getAllSkills(){
        return skillService.getAllSkills();
    }
}
