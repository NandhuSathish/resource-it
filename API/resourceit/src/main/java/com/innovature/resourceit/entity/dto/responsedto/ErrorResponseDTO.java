/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.innovature.resourceit.entity.dto.responsedto;

/**
 *
 * @author abdul.fahad
 */
public class ErrorResponseDTO {

    private String errorCode;
    private String errorMessage;

    public ErrorResponseDTO(String errorMessage, String errorCode) {

        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public ErrorResponseDTO() {

        this.errorCode = "0";
        this.errorMessage = "";
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

}
