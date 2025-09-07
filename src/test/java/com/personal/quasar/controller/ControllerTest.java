package com.personal.quasar.controller;

import com.personal.quasar.UnitTest;
import lombok.Getter;
import lombok.Setter;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@WithMockUser(username = "user@test.com")
public abstract class ControllerTest extends UnitTest {
    private static final String USER_NAME = "user@test.com";

    @Mock
    @Getter
    private UserDetailsService userDetailsService;

    @Getter
    @Setter
    private MockMvc mockMvc;

    protected String getUserName() {
        return USER_NAME;
    }
}
