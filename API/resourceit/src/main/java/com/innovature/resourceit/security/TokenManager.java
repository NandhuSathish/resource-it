/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.innovature.resourceit.security;

import com.innovature.resourceit.entity.dto.responsedto.JwtResponseDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 *
 * @author abdul.fahad
 */
@Getter
@Setter
@Component
public class TokenManager implements Serializable {

    private static final long serialVersionUID = 7008375124389347049L;
    @Value("${jwt.access.secret}")
    private String accessTokenSecretKey;
    @Value("${jwt.refresh.secret}")
    private String refreshTokenSecretKey;
    @Value("${jwt.expirationDateInMs}")
    private int jwtExpirationInMs;
    @Value("${jwt.refreshExpirationDateInMs}")
    private int refreshExpirationDateInMs;

    public JwtResponseDTO generateJwtToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        String accessToken = Jwts.builder().setClaims(claims).setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMs))
                .signWith(SignatureAlgorithm.HS512, accessTokenSecretKey).compact();

        String refreshToken = Jwts.builder().setClaims(claims).setSubject(userDetails.getUsername()).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + refreshExpirationDateInMs))
                .signWith(SignatureAlgorithm.HS512, refreshTokenSecretKey).compact();
        return new JwtResponseDTO(accessToken, refreshToken);
    }

    public Boolean validateJwtToken(String token, UserDetails userDetails) {
        String username = getUsernameFromToken(token);
        Claims claims = Jwts.parser().setSigningKey(accessTokenSecretKey).parseClaimsJws(token).getBody();
        Boolean isTokenExpired = claims.getExpiration().before(new Date());
        return (username.equals(userDetails.getUsername()) && !isTokenExpired);
    }

    public String getUsernameFromToken(String token) {
        final Claims claims = Jwts.parser().setSigningKey(accessTokenSecretKey).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }
    
    public String getUsernameFromRefreshToken(String token) {
        final Claims claims = Jwts.parser().setSigningKey(refreshTokenSecretKey).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }
}
