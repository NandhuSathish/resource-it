/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.innovature.resourceit.configuration;

import com.innovature.resourceit.security.JwtAuthenticationEntryPoint;
import com.innovature.resourceit.security.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author abdul.fahad
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

    private final JwtAuthenticationEntryPoint authenticationEntryPoint;
    private final JwtFilter filter;

    public static final String ADMIN = "ADMIN";
    public static final String HOD = "HOD";
    public static final String MANAGER = "MANAGER";

    @Autowired
    public WebSecurityConfig(JwtAuthenticationEntryPoint authenticationEntryPoint, JwtFilter filter) {
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.filter = filter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeHttpRequests()
                .requestMatchers("/api/v1/login").permitAll()
                .requestMatchers("/api/v2/**").permitAll()
                .and()
                .authorizeHttpRequests()
                .requestMatchers(HttpMethod.POST, "/api/v1/role").hasAnyAuthority(ADMIN)
                .requestMatchers(HttpMethod.POST, "/api/v1/department").hasAnyAuthority(ADMIN)
                .anyRequest().authenticated();

        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/v1/**") // Cleaned up the path pattern
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH") // Specify allowed HTTP methods
                        // .allowedOrigins("http://10.5.13.110:3000","http://10.5.17.110:3000","*")
                        .allowedOriginPatterns("*")
                        .allowCredentials(true);
            }
        };
    }
}
