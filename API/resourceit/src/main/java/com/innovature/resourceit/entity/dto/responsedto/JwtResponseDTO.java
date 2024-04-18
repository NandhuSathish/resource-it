/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.innovature.resourceit.entity.dto.responsedto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author abdul.fahad
 */
@Getter
@Setter
public class JwtResponseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String email;
    private String accessToken;
    private String refreshToken;
    private String userName;
    private Integer role;
    private String pictureUrl;
    private long expiredTime;
    private Integer resourceId;

    public JwtResponseDTO() {
    }

    public JwtResponseDTO(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
