/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.innovature.resourceit.exceptionhandler;

import org.springframework.security.authentication.BadCredentialsException;

/**
 *
 * @author abdul.fahad
 */
public class InvalidUserException extends Exception {

    public InvalidUserException() {
    }

    public InvalidUserException(String message) {
        super(message);
    }

    public InvalidUserException(String message, BadCredentialsException e) {
        super(message, e);
    }

}
