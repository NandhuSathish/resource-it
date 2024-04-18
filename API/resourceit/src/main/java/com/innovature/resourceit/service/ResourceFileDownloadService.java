/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.innovature.resourceit.service;

import com.innovature.resourceit.entity.dto.requestdto.ResourceDownloadFilterRequestDTO;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author abdul.fahad
 */
public interface ResourceFileDownloadService {

    public void resourceExcelDownload(HttpServletResponse response, ResourceDownloadFilterRequestDTO requestDTO);

}
