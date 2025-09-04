package com.personal.quasar.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.personal.quasar.BaseTests;
import com.personal.quasar.model.dto.UserDTO;
import com.personal.quasar.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.neo4j.AutoConfigureDataNeo4j;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTest extends BaseTests {
    @Mock
    UserService userService;
    MockMvc mockMvc;

    @InjectMocks
    UserController userController;

    @BeforeEach
    public void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }
    @Test
    @Disabled
    public void updateTest() throws Exception {
        String id = "abc";
        UserDTO userDTO = new UserDTO();
        userDTO.setId(id);
        userDTO.setFirstName("John");
        userDTO.setLastName("Doe");
        when(userService.update(id, userDTO)).thenReturn(userDTO);
        mockMvc.perform(put("/api/v1/user/{id}", id)
                .contentType("application/json")
                .content(new ObjectMapper().writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));

//        Assertions.assertNotNull(result);
//        Assertions.assertEquals(id, result.getBody().getId());
    }


}
