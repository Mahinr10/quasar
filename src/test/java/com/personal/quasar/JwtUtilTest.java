package com.personal.quasar;

import com.personal.quasar.model.dto.UserDTO;
import com.personal.quasar.util.JwtUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;

public class JwtUtilTest extends UnitTest {
    @Autowired
    private JwtUtil jwtUtil;

    @Test
    public void accessTokenTest() {
        var user = getUser();
        String token = jwtUtil.generateAccessToken(user);
        var result = jwtUtil.extractUser(token);
        Assertions.assertEquals(user.getEmail(), result.getEmail());
        Assertions.assertEquals(user.getUserRole(), result.getUserRole());
    }

    private UserDTO getUser() {
        var user = new UserDTO();
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setEmail("firstName.lastName@test.com");
        user.setUserRole(com.personal.quasar.model.enums.UserRole.USER);
        return user;
    }
}
