package com.innovature.resourceit.service.resource;

import com.innovature.resourceit.entity.Resource;
import com.innovature.resourceit.entity.dto.responsedto.ManagerListResponseDTO;
import com.innovature.resourceit.repository.ResourceRepository;
import com.innovature.resourceit.service.impli.ResourceServiceImpli;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.context.MessageSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@ContextConfiguration(classes = GetManagersTest.class)
class GetManagersTest {

    @InjectMocks
    ResourceServiceImpli service;

    @InjectMocks
    ResourceServiceImpli serviceTest;
    @Mock
    ResourceRepository resourceRepository;

    @Mock
    MessageSource messageSource;

    @BeforeEach
    public void setUp() {
        service = new ResourceServiceImpli(messageSource, resourceRepository, null, null, null, null);
        serviceTest = new ResourceServiceImpli(messageSource, resourceRepository, null, null, null);

    }

    @Test
    void testGetManagers() {
        // Mock data
        Resource resource1 = new Resource();
        resource1.setId(1);
        resource1.setName("Manager1");

        Resource resource2 = new Resource();
        resource2.setId(2);
        resource2.setName("Manager2");

        List<Resource> mockResourceList = Arrays.asList(resource1, resource2);

        // Mock the behavior of resourceRepository.findByRoleIdInAndStatus
        when(resourceRepository.findByRoleIdInAndStatus(List.of(2, 3, 5, 4), (byte) 1)).thenReturn(mockResourceList);

        // Call the method to be tested
        List<ManagerListResponseDTO> managerList = service.getManagers();

        // Assertions
        assertEquals(2, managerList.size());

        ManagerListResponseDTO dto1 = managerList.get(0);
        assertEquals(1, dto1.getId());
        assertEquals("Manager1", dto1.getName());

        ManagerListResponseDTO dto2 = managerList.get(1);
        assertEquals(2, dto2.getId());
        assertEquals("Manager2", dto2.getName());
    }
}
