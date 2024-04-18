/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.innovature.resourceit.entity.dto.responsedto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author abdul.fahad
 */
@Getter
@Setter
public class ResourceListResponseDTO {

    private int currentPage;

    private int totalPages;

    private long totalElements;

    List<ResourceListingResponseDTO> resourceListingResponseDTOs;
}
