/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.innovature.resourceit.service;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author abdul.fahad
 */
public interface ResourceFileUploadService {
    
    public String resourceExcelUpload(MultipartFile file) throws IOException;
    
}
