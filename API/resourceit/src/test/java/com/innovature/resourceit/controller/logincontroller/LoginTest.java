/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.innovature.resourceit.controller.logincontroller;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.innovature.resourceit.controller.LoginController;
import com.innovature.resourceit.entity.dto.responsedto.JwtResponseDTO;
import com.innovature.resourceit.entity.dto.responsedto.RefreshTokenDTO;
import com.innovature.resourceit.entity.dto.responsedto.RefreshTokenResponseDTO;
import com.innovature.resourceit.exceptionhandler.BadRequestException;
import com.innovature.resourceit.exceptionhandler.UserDisabledException;
import com.innovature.resourceit.security.JwtUserDetailsService;
import com.innovature.resourceit.security.TokenManager;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ContextConfiguration;

/**
 *
 * @author abdul.fahad
 */
@SpringBootTest
@ContextConfiguration(classes = LoginTest.class)
@RunWith(MockitoJUnitRunner.class)
class LoginTest {

    @InjectMocks
    private LoginController loginController;

    @Mock
    private TokenManager tokenManager;

    @Mock
    private JwtUserDetailsService jwtUserDetailsService;

    private String email;

    private String role;

    private UserDetails userDetails;

    private JwtResponseDTO jwtResponse;

    @Mock
    private GoogleIdTokenVerifier verifier;

    @Mock
    private GoogleIdTokenVerifier.Builder verifierBuilder;

    @Mock
    GoogleIdToken idToken;

    @Mock
    HttpTransport transport;

    @Mock
    JsonFactory jsonFactory;

    private String clientId;
    
    @Mock
    MessageSource messageSource;

//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        email = "fahad@gmail.com";
//        role = "ADMIN";
//        userDetails = new User(email, "", Arrays.asList(new SimpleGrantedAuthority(role)));
//        Header header = Mockito.mock(Header.class);
//        Payload payload = Mockito.mock(Payload.class);
//        payload.setEmail(email);
//        payload.setSubject("ftwftdw121");
//        byte[] signatureBytes = "your_signature_bytes_here".getBytes(); // Replace with your signature bytes
//        byte[] signedContentBytes = "your_signed_content_bytes_here".getBytes(); // Replace with your signed content bytes
//
//        idToken = new GoogleIdToken(header, payload, signatureBytes, signedContentBytes);
//        clientId = "rqwqtywhqnxzxxzm-xjznsdnussjdijs-shdsuhu";
//        loginController = new LoginController(jwtUserDetailsService, tokenManager, verifier, idToken, transport, jsonFactory, clientId);
//
//        jwtResponse = new JwtResponseDTO();
//        jwtResponse.setEmail(email);
//        jwtResponse.setAccessToken("JHSJHDJD-SKDNSKDKSsjdnsdns-121skd");
//        jwtResponse.setRefreshToken("JHAHAOHSPIAJ-DNSDKNSKDNS-17617sdnsj");
//        jwtResponse.setExpiredTime(System.currentTimeMillis() + 3600000);
//
//    }
//    @Test
//    void login() throws GeneralSecurityException, IOException {
//
//        SSORequestDTO form = new SSORequestDTO("jhasuasjhisjaoks-ksjdisjsjdijssihdishi");
//
////        verifier = Mockito.mock(GoogleIdTokenVerifier.class);
//            MockedStatic<GoogleIdTokenVerifier> mocked = Mockito.mockStatic(GoogleIdTokenVerifier.class);
//            
////        Mockito.when(verifierBuilder.setAudience(Mockito.any())).thenReturn(verifierBuilder);
////        Mockito.when(verifierBuilder.build()).thenReturn(verifier);
////        Mockito.when(verifier.verify(Mockito.any())).thenReturn(idToken);
//                        mocked.when(()->GoogleIdTokenVerifier.Builder(Mockito.any(),Mockito.any())).thenReturn(mocked);
//
////            mocked.when(()->GoogleIdTokenVerifier.verify(ArgumentMatchers.anyString())).thenReturn(idToken);
//        
//
//        Mockito.when(jwtUserDetailsService.loadUserByUsername(email)).thenReturn(userDetails);
//        UsernamePasswordAuthenticationToken userPass = new UsernamePasswordAuthenticationToken(email, null, userDetails.getAuthorities());
//        SecurityContextHolder.getContext().setAuthentication(userPass);
//        Mockito.when(tokenManager.generateJwtToken(userDetails)).thenReturn(jwtResponse);
//
//        ResponseEntity<Object> response = loginController.login(form);
//
//        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
//    }
    @Test
    void refreshToken() throws Exception {
        String username = "test@gmail.com";
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");
        org.springframework.security.core.userdetails.UserDetails userDetail = new User(
                username, 
                "[PROTECTED]", 
                Collections.singletonList(authority) 
        );
        JwtResponseDTO jwtResponseDTO = new JwtResponseDTO("newAccessToken", "newRefreshToken");
        RefreshTokenDTO refreshTokenDTO = new RefreshTokenDTO();
        refreshTokenDTO.setRefreshToken("oldRefreshToken");
        when(tokenManager.getUsernameFromRefreshToken(refreshTokenDTO.getRefreshToken())).thenReturn(username);
        when(jwtUserDetailsService.loadUserByUsername(username)).thenReturn(userDetail);
        when(tokenManager.generateJwtToken(userDetail)).thenReturn(jwtResponseDTO);

        ResponseEntity<Object> responseEntity = loginController.getAccessToken(refreshTokenDTO);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

    }
    
//    @Test
//    void refreshTokenNullCase() throws Exception {
//        RefreshTokenDTO refreshTokenDTO = new RefreshTokenDTO();
//        refreshTokenDTO.setRefreshToken("");
//        when(messageSource.getMessage(eq("REFRESH_TOKEN_NEEDED"), null, Locale.ENGLISH))
//                .thenReturn("1007-Refresh token needed");
//
//        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
//            loginController.getAccessToken(refreshTokenDTO);
//        });
//
//        assertNotNull(exception);
//        assertEquals("1007-Refresh token needed", exception.getBody().getDetail());
//
//    }

}
