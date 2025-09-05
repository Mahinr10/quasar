package com.personal.quasar.controller;

import com.personal.quasar.BaseTests;
import com.personal.quasar.service.UserService;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@WithMockUser(username = "user@test.com")
public abstract class ControllerTest extends BaseTests {
    private static final String USER_NAME = "user@test.com";
    @Mock
    @Getter
    private UserService userService;

    @Getter
    @Setter
    private MockMvc mockMvc;

    protected String getUserName() {
        return USER_NAME;
    }
}
