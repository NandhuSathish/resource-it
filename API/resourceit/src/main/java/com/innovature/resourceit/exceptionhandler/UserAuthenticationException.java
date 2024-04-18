/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.innovature.resourceit.exceptionhandler;

import org.springframework.security.core.AuthenticationException;

/**
 *
 * @author abdul.fahad
 */
public class UserAuthenticationException extends Exception{
    
     public UserAuthenticationException() {
    }
    
    public UserAuthenticationException(String message) {
        super(message);
    }
    
    public UserAuthenticationException(String message, AuthenticationException e) {
        super(message, e);
    }
    
}
