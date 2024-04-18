package com.innovature.resourceit.securitytest;

import com.innovature.resourceit.entity.Resource;
import com.innovature.resourceit.entity.Role;
import com.innovature.resourceit.repository.ResourceRepository;
import com.innovature.resourceit.security.JwtUserDetailsService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtUserDetailsTest  {

    @InjectMocks
    private JwtUserDetailsService jwtUserDetailsService;

    @Mock
    private ResourceRepository resourceRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoadUserByUsernameWhenUserExists() {

        String email = "user@example.com";
        Resource resource = new Resource();
        resource.setEmail(email);
        resource.setRole(new Role("ADMIN"));
        when(resourceRepository.findByEmailAndStatus(email, Resource.Status.ACTIVE.value))
                .thenReturn(Optional.of(resource));

        UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(email);

        assertNotNull(userDetails);
        assertEquals(resource.getEmail(), userDetails.getUsername());
    }

    @Test
    void testLoadUserByUsernameWhenUserDoesNotExist() {

        String email = "nonexistent@example.com";
        when(resourceRepository.findByEmailAndStatus(email, Resource.Status.ACTIVE.value))
                .thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> jwtUserDetailsService.loadUserByUsername(email));
    }

}

