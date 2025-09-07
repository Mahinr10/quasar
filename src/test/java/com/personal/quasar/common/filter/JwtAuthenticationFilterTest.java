package com.personal.quasar.common.filter;

import com.personal.quasar.UnitTest;
import com.personal.quasar.model.dto.UserAuthorityDTO;
import com.personal.quasar.model.dto.UserDTO;
import com.personal.quasar.model.dto.UserDetailsDTO;
import com.personal.quasar.model.enums.UserRole;
import com.personal.quasar.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

public class JwtAuthenticationFilterTest extends UnitTest {
    @Mock
    JwtUtil jwtUtil;

    @Mock
    UserDetailsService userDetailsService;

    @InjectMocks
    JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    public void doFilterTest() throws Exception {
        var request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer token");

        var user = new UserDTO();

        user.setUserRole(UserRole.USER);
        user.setEmail("user@test.com");
        when(jwtUtil.extractUser("token")).thenReturn(user);

        var filterChain = mock(FilterChain.class);
        doNothing().when(filterChain).doFilter(any(HttpServletRequest.class), any(HttpServletResponse.class));
        when(jwtUtil.extractUser("token")).thenReturn(user);

        UserDetailsDTO userDetailsDTO = new UserDetailsDTO();
        userDetailsDTO.setUsername(user.getEmail());
        var authority = new UserAuthorityDTO();
        authority.setAuthority(user.getUserRole().name());
        userDetailsDTO.setAuthorities(List.of(authority));

        when(jwtUtil.validateToken("token")).thenReturn(true);

        when(userDetailsService.loadUserByUsername(user.getEmail())).thenReturn(userDetailsDTO);

        try(MockedStatic<SecurityContextHolder> securityContextHolderMock = mockStatic(SecurityContextHolder.class)) {
            var authentication = mock(SecurityContext.class);
            when(authentication.getAuthentication()).thenReturn(null);
            securityContextHolderMock.when(SecurityContextHolder::getContext).thenReturn(authentication);
            securityContextHolderMock.when(() -> SecurityContextHolder.setContext(any())).then(invocationOnMock -> null);
            jwtAuthenticationFilter.doFilterInternal(request, mock(HttpServletResponse.class), filterChain);
        }
    }
}
