package com.innovature.resourceit.service.impli;

import com.innovature.resourceit.entity.Skill;
import com.innovature.resourceit.repository.SkillRepository;
import com.innovature.resourceit.service.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkillServiceImpli implements SkillService {

    @Autowired
    SkillRepository skillRepository;

    @Override
    public List<Skill> getAllSkills() {
        return skillRepository.findAll();
    }
}
