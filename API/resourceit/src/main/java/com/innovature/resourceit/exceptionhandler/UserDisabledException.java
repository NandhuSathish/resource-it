/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.innovature.resourceit.exceptionhandler;

import org.springframework.security.authentication.DisabledException;

/**
 *
 * @author abdul.fahad
 */
public class UserDisabledException extends Exception {
    
    public UserDisabledException() {
    }
    
    public UserDisabledException(String message) {
        super(message);
    }
    
    public UserDisabledException(String message, DisabledException e) {
        super(message, e);
    }
    
}
