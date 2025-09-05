package com.personal.quasar.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.personal.quasar.model.dto.UserDTO;
import com.personal.quasar.model.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTest extends ControllerTest {
    @InjectMocks
    UserController userController;

    @BeforeEach
    public void executeBeforeEach() {
        var mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        setMockMvc(mockMvc);
    }
    @Test
    public void updateTest() throws Exception {
        var id = "abc";
        var userDTO = getUserDTO();

        when(getUserService().update(id, userDTO)).thenReturn(userDTO);
        when(getUserService().loadUserByUsername("testUser")).thenReturn(userDTO);
        getMockMvc().perform(put("/api/v1/user/{id}", id)
                .contentType("application/json")
                .content(new ObjectMapper().writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    private User getUser() {
        User user = new User();
        user.setFirstName("Jane");
        user.setLastName("Smith");
        user.setEmail(getUserName());
        user.setPassword("password");
        return user;
    }
    private UserDTO getUserDTO() {
        UserDTO userDTO = new UserDTO();
        userDTO.setId("abc");
        userDTO.setFirstName("John");
        userDTO.setLastName("Doe");
        userDTO.setEmail(getUserName());
        return userDTO;
    }


}
