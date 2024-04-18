package com.innovature.resourceit.entity.dto.response;

import static org.junit.jupiter.api.Assertions.*;

import com.innovature.resourceit.entity.dto.responsedto.RefreshTokenDTO;
import com.innovature.resourceit.entity.dto.responsedto.RefreshTokenResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RefreshTokenDTOTest {

    private RefreshTokenDTO refreshTokenDTO;
    private RefreshTokenResponseDTO refreshTokenResponseDTO;

    @BeforeEach
    void setUp() {
        refreshTokenDTO = new RefreshTokenDTO();
        refreshTokenResponseDTO = new RefreshTokenResponseDTO();
    }

    @Test
    void testGettersAndSettersForRefreshTokenDTO() {
        refreshTokenDTO.setRefreshToken("newRefreshToken");
        assertEquals("newRefreshToken", refreshTokenDTO.getRefreshToken());
    }
    
    @Test
    void testGettersAndSettersForRefreshTokenResponseDTO() {
        refreshTokenResponseDTO.setAccessToken("newAccessToken");
        refreshTokenResponseDTO.setRefreshToken("newRefreshToken");
        assertEquals("newRefreshToken", refreshTokenResponseDTO.getRefreshToken());
        assertEquals("newAccessToken", refreshTokenResponseDTO.getAccessToken());
    }
}

