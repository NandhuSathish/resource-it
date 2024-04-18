/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.innovature.resourceit.controller;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.innovature.resourceit.entity.Resource;
import com.innovature.resourceit.entity.dto.requestdto.SSORequestDTO;
import com.innovature.resourceit.entity.dto.responsedto.ErrorResponseDTO;
import com.innovature.resourceit.entity.dto.responsedto.JwtResponseDTO;
import com.innovature.resourceit.entity.dto.responsedto.RefreshTokenDTO;
import com.innovature.resourceit.entity.dto.responsedto.RefreshTokenResponseDTO;
import com.innovature.resourceit.exceptionhandler.BadRequestException;
import com.innovature.resourceit.exceptionhandler.InvalidUserException;
import com.innovature.resourceit.exceptionhandler.UserAuthenticationException;
import com.innovature.resourceit.exceptionhandler.UserDisabledException;
import com.innovature.resourceit.repository.ResourceRepository;
import com.innovature.resourceit.security.JwtUserDetailsService;
import com.innovature.resourceit.security.TokenManager;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.validation.Valid;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Locale;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * @author abdul.fahad
 */
@RestController
@RequestMapping(path = "/api/v1")
public class LoginController {

    @Autowired
    JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    TokenManager tokenManager;

    @Autowired
    MessageSource messageSource;

    @Autowired
    ResourceRepository resourceRepository;

    @Value("${client.id}")
    private String clientId;

    public LoginController(JwtUserDetailsService jwtUserDetailsService, TokenManager tokenManager) {

        this.jwtUserDetailsService = jwtUserDetailsService;
        this.tokenManager = tokenManager;
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@Valid @RequestBody SSORequestDTO form) throws GeneralSecurityException, IOException {
        String idTokenString = form.getToken();

        if (idTokenString == null) {
            throw new BadRequestException(messageSource.getMessage("GOOGLE_API_TOKEN", null, Locale.ENGLISH));
        }
        HttpTransport transport = new NetHttpTransport(); // Use NetHttpTransport for HTTP requests

        JsonFactory jsonFactory = new GsonFactory();
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                // Specify the CLIENT_ID of the app that accesses the backend:
                .setAudience(Collections.singletonList(clientId))
                // Or, if multiple clients access the backend:
                // .setAudience(Arrays.asList(CLIENT_ID_1, CLIENT_ID_2, CLIENT_ID_3))
                .build();

        GoogleIdToken idToken = verifier.verify(idTokenString);
        if (idToken == null) {
            throw new BadRequestException(messageSource.getMessage("GOOGLE_API_VERIFICATION_FAILED", null, Locale.ENGLISH));
        }

        Payload payload = idToken.getPayload();

        // Get profile information from payload
        String email = payload.getEmail();
        String pictureUrl=null;
        //check picture is null or not 
        if(payload.get("picture")!=null){
             pictureUrl = payload.get("picture").toString();
        }
        UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(email);
        Optional<Resource> optUser = resourceRepository.findByEmailAndStatus(email, Resource.Status.ACTIVE.value);
        String userName = optUser.isPresent() ? optUser.get().getName() : "";
        Integer role = optUser.isPresent() ? optUser.get().getRole().getId() : 7;
        Integer resourceId = optUser.map(Resource::getId).orElse(null);
        if (role.equals(Resource.Roles.RESOURCE.getId())) {
            return new ResponseEntity<>(new ErrorResponseDTO("Access Denied!", "1010"), HttpStatus.UNAUTHORIZED);
        }
        UsernamePasswordAuthenticationToken userPass = new UsernamePasswordAuthenticationToken(email, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(userPass);
        JwtResponseDTO jwtResponseDTO = tokenManager.generateJwtToken(userDetails);
        jwtResponseDTO.setUserName(userName);
        jwtResponseDTO.setRole(role);
        jwtResponseDTO.setPictureUrl(pictureUrl);
        jwtResponseDTO.setEmail(email);
        jwtResponseDTO.setResourceId(resourceId);
        return new ResponseEntity<>(jwtResponseDTO, HttpStatus.OK);
    }

    @PutMapping("/login")
    public ResponseEntity<Object> getAccessToken(@Valid @RequestBody RefreshTokenDTO refreshTokenDTO) throws UserDisabledException, InvalidUserException, UserAuthenticationException {

        try {
            if (("".equals(refreshTokenDTO.getRefreshToken().trim()) || refreshTokenDTO.getRefreshToken() == null)) {
                throw new BadRequestException(messageSource.getMessage("REFRESH_TOKEN_NEEDED", null, Locale.ENGLISH));
            } else {
                String username = tokenManager.getUsernameFromRefreshToken(refreshTokenDTO.getRefreshToken());
                UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(username);
                JwtResponseDTO jwtResponse = tokenManager.generateJwtToken(userDetails);
                RefreshTokenResponseDTO responseDTO = new RefreshTokenResponseDTO(jwtResponse.getAccessToken(), jwtResponse.getRefreshToken());
                return new ResponseEntity<>(responseDTO, HttpStatus.OK);
            }
        } catch (DisabledException e) {
            throw new UserDisabledException();
        } catch (ExpiredJwtException e) {
            return new ResponseEntity<>(new ErrorResponseDTO("Refresh token has expired", "1008"), HttpStatus.UNAUTHORIZED);
        } catch (ArrayIndexOutOfBoundsException e) {
            return new ResponseEntity<>(new ErrorResponseDTO("Invalid token", "1001"), HttpStatus.UNAUTHORIZED);
        } catch (BadCredentialsException e) {
            throw new InvalidUserException();
        } catch (AuthenticationException e) {
            throw new UserAuthenticationException();
        }
    }
}
