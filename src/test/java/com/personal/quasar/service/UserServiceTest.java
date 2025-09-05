package com.personal.quasar.service;

import com.personal.quasar.BaseTests;
import com.personal.quasar.dao.UserRepository;
import com.personal.quasar.exception.ImmutableFieldModificationException;
import com.personal.quasar.exception.ResourceDoesNotExistException;
import com.personal.quasar.model.dto.UserDTO;
import com.personal.quasar.model.entity.User;
import com.personal.quasar.model.mapper.UserMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class UserServiceTest extends BaseTests {
    @Mock
    UserRepository userRepository;

    @Mock
    UserMapper userMapper;

    @InjectMocks
    UserService userService;

    @Test void getTest() throws Exception {
        String id = "abc";
        var user = getUser(id);
        var userDTO = getUserDTO(id);

        when(userRepository.findByIdAndIsDeletedFalse(id)).thenReturn(Optional.of(user));
        when(userMapper.entityToDTO(user)).thenReturn(userDTO);

        var result = userService.get(id);

        Assertions.assertEquals(id, result.getId());
    }

    @Test void getWithInvalidIdTest(){
        String id = "abc";
        var user = getUser(id);
        var userDTO = getUserDTO(id);

        when(userRepository.findByIdAndIsDeletedFalse(id)).thenReturn(Optional.empty());

        var exception = Assertions.assertThrows(ResourceDoesNotExistException.class, () -> {
            userService.get(id);
        });

        Assertions.assertEquals(String.format(ResourceDoesNotExistException.MESSAGE, "user", id), exception.getMessage());
    }

    @Test
    public void updateTest() throws Exception {
        String id = "abc";
        var newUserDTO = getUserDTO(id);
        var user = getUser(id);
        var newUser = getUser(id);

        user.setFirstName("Jane");

        when(userRepository.findByIdAndIsDeletedFalse(id)).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenReturn(newUser);

        when(userMapper.entityToDTO(newUser)).thenReturn(newUserDTO);

        var result = userService.update(id, newUserDTO);

        Assertions.assertEquals(id, result.getId());
        Assertions.assertEquals("John", result.getFirstName());
        Assertions.assertEquals("Doe", result.getLastName());
        Assertions.assertEquals("john.doe@gmail.com", result.getEmail());
    }

    @Test()
    public void updateImmutableFieldsTest() throws Exception {
        String id = "abc";
        var userDTO = getUserDTO(id);
        var user = getUser(id);
        userDTO.setEmail("jane@gmail.com");
        when(userRepository.findByIdAndIsDeletedFalse(id)).thenReturn(Optional.of(user));
        var exception = Assertions.assertThrows(ImmutableFieldModificationException.class, () -> {
            userService.update(id, userDTO);
        });
        Assertions.assertEquals(String.format(ImmutableFieldModificationException.MESSAGE, "email"), exception.getMessage());
    }

    @Test
    public void updateWithSameFieldTest() throws Exception {
        String id = "abc";
        var userDTO = getUserDTO(id);
        var user = getUser(id);
        when(userRepository.findByIdAndIsDeletedFalse(id)).thenReturn(Optional.of(user));
        var result = userService.update(id, userDTO);
        Assertions.assertEquals(userDTO.getId(), result.getId());
    }

    @Test
    public void UpdateWithNonExistingUserTest() {
        String id = "abc";
        var userDTO = getUserDTO(id);
        when(userRepository.findByIdAndIsDeletedFalse(id)).thenReturn(Optional.empty());
        var exception = Assertions.assertThrows(ResourceDoesNotExistException.class, () -> {
            userService.update(id, userDTO);
        });
        Assertions.assertEquals(String.format(ResourceDoesNotExistException.MESSAGE, "user", id), exception.getMessage());
    }

    private User getUser(String id) {
        var user = new User();
        user.setId(id);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@gmail.com");
        return user;
    }

    private UserDTO getUserDTO(String id) {
        var userDTO = new UserDTO();
        userDTO.setId(id);
        userDTO.setFirstName("John");
        userDTO.setLastName("Doe");
        userDTO.setEmail("john.doe@gmail.com");
        return userDTO;
    }

}
