/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.innovature.resourceit.entity.dto.requestdto;

import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 *
 * @author abdul.fahad
 */
public class JwtRequestDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    @NotBlank(message = "email.not.entered")
    private String email;
    @NotBlank(message = "password.not.entered")
    private String password;
    public JwtRequestDTO() {
    }

    public JwtRequestDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
