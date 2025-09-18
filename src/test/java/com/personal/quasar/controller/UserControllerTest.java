package com.personal.quasar.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.personal.quasar.common.exception.GlobalExceptionHandler;
import com.personal.quasar.common.exception.UnprivilegedToModificationException;
import com.personal.quasar.model.dto.UserAuthorityDTO;
import com.personal.quasar.model.dto.UserDTO;
import com.personal.quasar.model.dto.UserDetailsDTO;
import com.personal.quasar.model.entity.User;
import com.personal.quasar.model.enums.UserRole;
import com.personal.quasar.service.impl.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTest extends ControllerTest {
    @InjectMocks
    UserController userController;

    @Mock
    UserService userService;

    @BeforeEach
    public void executeBeforeEach() {
        Mockito.reset(userService);
        var mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        setMockMvc(mockMvc);
    }
    @Test
    public void updateTest() throws Exception {
        var id = "abc";
        var userDTO = getUserDTO();

        when(userService.update(id, userDTO)).thenReturn(userDTO);
        when(getUserDetailsService().loadUserByUsername("testUser")).thenReturn(getUserDetailsDTO());
        getMockMvc().perform(put("/api/v1/user/{id}", id)
                .contentType("application/json")
                .content(new ObjectMapper().writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    public void updateUserRoleWithoutAdminPrivilegeTest() throws Exception {
        var id = "abc";
        var userDTO = getUserDTO();
        userDTO.setUserRole(UserRole.ADMIN);

        UnprivilegedToModificationException unprivilegedToModificationException =
                new UnprivilegedToModificationException("message");

        doThrow(unprivilegedToModificationException).when(userService).update(eq(id), any(UserDTO.class));
        when(getUserDetailsService().loadUserByUsername("testUser")).thenReturn(getUserDetailsDTO());
        getMockMvc().perform(put("/api/v1/user/{id}", id)
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(userDTO)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("message"));
    }

    private User getUser() {
        User user = new User();
        user.setFirstName("Jane");
        user.setLastName("Smith");
        user.setEmail(getUserName());
        user.setPassword("password");
        user.setUserRole(UserRole.USER);
        return user;
    }
    private UserDTO getUserDTO() {
        UserDTO userDTO = new UserDTO();
        userDTO.setId("abc");
        userDTO.setFirstName("John");
        userDTO.setLastName("Doe");
        userDTO.setEmail(getUserName());
        userDTO.setUserRole(UserRole.USER);
        return userDTO;
    }

    private UserDetailsDTO getUserDetailsDTO() {
        UserDetailsDTO userDetailsDTO = new UserDetailsDTO();
        userDetailsDTO.setUsername("user@test.com");
        userDetailsDTO.setPassword("password");
        UserAuthorityDTO userAuthorityDTO = new UserAuthorityDTO();
        userAuthorityDTO.setAuthority(UserRole.USER.name());
        userDetailsDTO.setAuthorities(List.of(userAuthorityDTO));
        return userDetailsDTO;
    }

}
