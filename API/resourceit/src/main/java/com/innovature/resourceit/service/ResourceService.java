/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.innovature.resourceit.service;

import com.innovature.resourceit.entity.dto.requestdto.ResourceRequestDTO;
import com.innovature.resourceit.entity.dto.responsedto.ManagerListResponseDTO;
import com.innovature.resourceit.entity.dto.responsedto.ResourceResponseDTO;
import com.innovature.resourceit.entity.dto.requestdto.ResourceFilterRequestDTO;
import com.innovature.resourceit.entity.dto.responsedto.PagedResponseDTO;
import com.innovature.resourceit.entity.dto.responsedto.ResourceListingResponseDTO;
import com.innovature.resourceit.exceptionhandler.UserNotFoundException;

import java.util.List;


/**
 *
 * @author abdul.fahad
 */
public interface ResourceService {

    public void add(ResourceRequestDTO dto);

    public PagedResponseDTO<ResourceListingResponseDTO> getResources(ResourceFilterRequestDTO requestDTO);

    public ResourceResponseDTO getResourceById(Integer id);

    public void updateResource(Integer id, ResourceRequestDTO dto);

    public void deleteResource(Integer id) throws UserNotFoundException;

    List<ManagerListResponseDTO> getManagers();
}
