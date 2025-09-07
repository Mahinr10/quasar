package com.personal.quasar.service.impl;

import com.personal.quasar.UnitTest;
import com.personal.quasar.common.exception.ResourceDoesNotExistException;
import com.personal.quasar.dao.UserRepository;
import com.personal.quasar.model.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static com.personal.quasar.util.RepositoryErrorMessageConstants.USER_ENTITY;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

public class UserDetailsAdapterTest extends UnitTest {
    @Mock
    private UserService userService;
    @InjectMocks
    private UserDetailsAdapter userDetailsAdapter;
    @Test
    public void loadUserByUsernameTest() throws ResourceDoesNotExistException {
        var user = getUser();
        when(userService.getUserByEmail(user.getEmail())).thenReturn(user);
        var userDetails = userDetailsAdapter.loadUserByUsername(user.getEmail());
        Assertions.assertEquals(user.getEmail(), userDetails.getUsername());
    }

    @Test
    public void loadUserByUsernameNonExistingUserTest() throws ResourceDoesNotExistException {
        var user = getUser();
        var exception = new ResourceDoesNotExistException(USER_ENTITY, user.getEmail());
        doThrow(exception)
                .when(userService).getUserByEmail(user.getEmail());
        var result = Assertions.assertThrows(
                UsernameNotFoundException.class,
                () -> userDetailsAdapter.loadUserByUsername(user.getEmail())
        );

        Assertions.assertEquals(exception.getMessage(), result.getMessage());
    }


    private User getUser() {
        var user = new User();
        user.setEmail("user@test.com");
        user.setPassword("password");
        return user;
    }
}
