/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.innovature.resourceit.configuration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 *
 * @author abdul.fahad
 */
@SpringBootTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
class WebConfigurationTest {
    
    @Autowired
    private MockMvc mockMvc;

//    @Test
//    void givenRequest_whenCorsIsConfigured_thenSuccess() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/login")
//                .header("Origin", "http://example.com")
//                .header("Access-Control-Request-Method", "GET"))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.header().exists("Access-Control-Allow-Origin"))
//                .andExpect(MockMvcResultMatchers.header().string("Access-Control-Allow-Credentials", "true"));
//    }
//    
//    @Test
//    void testFilterChainConfiguration() throws Exception {
//        
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/login"))
//                .andExpect(MockMvcResultMatchers.status().isOk());
//
//    }
    
}
