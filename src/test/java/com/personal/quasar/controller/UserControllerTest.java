package com.personal.quasar.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.personal.quasar.BaseTests;
import com.personal.quasar.dao.UserRepository;
import com.personal.quasar.filter.JwtAuthenticationFilter;
import com.personal.quasar.model.dto.UserDTO;
import com.personal.quasar.model.entity.User;
import com.personal.quasar.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.neo4j.AutoConfigureDataNeo4j;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WithMockUser(username = "testuser", roles = {"USER"})
public class UserControllerTest extends BaseTests{
    @Mock
    UserService userService;
    MockMvc mockMvc;
    @InjectMocks
    UserController userController;

    @BeforeEach
    public void executeBeforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }
    @Test
    public void updateTest() throws Exception {
        String id = "abc";
        UserDTO userDTO = new UserDTO();
        userDTO.setId(id);
        userDTO.setFirstName("John");
        userDTO.setLastName("Doe");
        userDTO.setEmail("testUser");

        User user = new User();
        user.setFirstName("Jane");
        user.setLastName("Smith");
        user.setEmail("testUser");

        when(userService.update(id, userDTO)).thenReturn(userDTO);
        when(userService.loadUserByUsername("testUser")).thenReturn(user);
        mockMvc.perform(put("/api/v1/user/{id}", id)
                .contentType("application/json")
                .content(new ObjectMapper().writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }


}
