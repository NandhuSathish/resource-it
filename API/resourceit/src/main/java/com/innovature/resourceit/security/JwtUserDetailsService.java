/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.innovature.resourceit.security;


import com.innovature.resourceit.entity.Resource;
import com.innovature.resourceit.repository.ResourceRepository;

import java.util.Arrays;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author abdul.fahad
 */
@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    ResourceRepository resourceRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Resource> optUser = resourceRepository.findByEmailAndStatus(email, (byte) 1);
        if (optUser.isPresent()) {
            return new org.springframework.security.core.userdetails
                    .User(optUser.get().getEmail(), "", Arrays.asList(new SimpleGrantedAuthority(optUser.get().getRole().getName())));
        } else {
            throw new UsernameNotFoundException("Can't find user with email :- " + email);
        }
    }

}
