/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.innovature.resourceit.exceptionhandler;

/**
 *
 * @author abdul.fahad
 */
public class UserNotFoundException extends Exception{

    public UserNotFoundException() {
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}

