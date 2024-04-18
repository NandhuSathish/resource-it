package com.innovature.resourceit.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface SkillProficiencyFileUpload {
    public String skillProficiencyFileUpload(MultipartFile file) throws IOException;
}
