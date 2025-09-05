package com.personal.quasar.controller;

import com.personal.quasar.UnitTest;
import com.personal.quasar.service.impl.UserService;
import lombok.Getter;
import lombok.Setter;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@WithMockUser(username = "user@test.com")
public abstract class ControllerTest extends UnitTest {
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
