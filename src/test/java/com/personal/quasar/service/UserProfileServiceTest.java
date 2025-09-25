package com.personal.quasar.service;

import com.personal.quasar.UnitTest;
import com.personal.quasar.model.dto.UserAuthorityDTO;
import com.personal.quasar.model.enums.UserRole;
import com.personal.quasar.service.impl.UserProfileService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

public class UserProfileServiceTest extends UnitTest {

    Authentication authentication;
    SecurityContext securityContext;
    @BeforeEach
    public void executeBeforeEach() {
        authentication = mock(Authentication.class);
        securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
    }
    @InjectMocks
    private UserProfileService userProfileService;

    @Test
    public void getActiveUserIdTest() {
        try(MockedStatic<SecurityContextHolder> mockedSecurityContextHolder = mockStatic(SecurityContextHolder.class)){
            mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            var email = "user@test.com";
            when(authentication.getName()).thenReturn("user@test.com");
            var result = userProfileService.getActiveUserId();
            Assertions.assertEquals(email, result);
        } catch(Exception e) {
            fail();
        }
    }

    @Test
    public void getActiveUserIdForSystemTest() {
        try(MockedStatic<SecurityContextHolder> mockedSecurityContextHolder = mockStatic(SecurityContextHolder.class)){
            mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(authentication.getName()).thenReturn(null);
            var result = userProfileService.getActiveUserId();
            Assertions.assertEquals("System", result);
        } catch(Exception e) {
            fail();
        }
    }

    @Test
    public void isUserWithRoleTest() {
        var authority = mock(Collection.class);
        when(authority.contains(any())).thenReturn(true);
        when(authentication.getAuthorities()).thenReturn(authority);
        try(MockedStatic<SecurityContextHolder> mockedSecurityContextHolder = mockStatic(SecurityContextHolder.class)) {
            mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            Assertions.assertTrue(userProfileService.isUsersWithRole(UserRole.ADMIN));
        }
    }

    @Test
    public void isUserWithoutRoleTest() {
        var authority = mock(Collection.class);
        when(authority.contains(any())).thenReturn(false);
        when(authentication.getAuthorities()).thenReturn(authority);
        try(MockedStatic<SecurityContextHolder> mockedSecurityContextHolder = mockStatic(SecurityContextHolder.class)) {
            mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            Assertions.assertFalse(userProfileService.isUsersWithRole(UserRole.ADMIN));
        }
    }
}
