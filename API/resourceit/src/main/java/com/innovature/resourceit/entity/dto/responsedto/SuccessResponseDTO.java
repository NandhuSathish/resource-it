/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.innovature.resourceit.entity.dto.responsedto;

/**
 *
 * @author abdul.fahad
 */
public class SuccessResponseDTO {

    private String successCode;
    private String successMessage;

    public SuccessResponseDTO() {
    }

    public SuccessResponseDTO(String successCode, String successMessage) {
        this.successCode = successCode;
        this.successMessage = successMessage;
    }

    public String getSuccessCode() {
        return successCode;
    }

    public void setSuccessCode(String successCode) {
        this.successCode = successCode;
    }

    public String getSuccessMessage() {
        return successMessage;
    }

    public void setSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
    }


}
