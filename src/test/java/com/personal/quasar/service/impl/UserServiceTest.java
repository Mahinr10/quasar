package com.personal.quasar.service.impl;

import com.personal.quasar.UnitTest;
import com.personal.quasar.common.exception.InvalidFieldException;
import com.personal.quasar.common.exception.UnprivilegedToModificationException;
import com.personal.quasar.dao.UserRepository;
import com.personal.quasar.common.exception.ImmutableFieldModificationException;
import com.personal.quasar.common.exception.ResourceDoesNotExistException;
import com.personal.quasar.model.dto.UserDTO;
import com.personal.quasar.model.entity.TimeZone;
import com.personal.quasar.model.entity.User;
import com.personal.quasar.model.enums.UserRole;
import com.personal.quasar.model.mapper.UserMapper;
import com.personal.quasar.service.AuditService;
import com.personal.quasar.service.TimeZoneService;
import com.personal.quasar.service.UserProfileFacade;
import com.personal.quasar.service.impl.UserService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.lang.reflect.Method;
import java.util.Optional;
import java.util.Set;

import static com.personal.quasar.util.RepositoryErrorMessageConstants.USER_ENTITY;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserServiceTest extends UnitTest {
    @MockitoBean
    UserRepository userRepository;

    @Mock
    UserMapper userMapper;

    @Mock
    AuditService auditService;

    @Mock
    UserProfileFacade userProfileFacade;

    @Mock
    TimeZoneService timeZoneService;

    @InjectMocks
    UserService userService;

    Validator validator;

    @BeforeEach
    public void executeBeforeEach() {
        doNothing().when(auditService).populateAuditFields(any());
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

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

        Assertions.assertEquals(String.format(ResourceDoesNotExistException.MESSAGE, USER_ENTITY, id), exception.getMessage());
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
        when(userMapper.dtoToEntity(newUserDTO)).thenReturn(newUser);

        var result = userService.update(id, newUserDTO);

        Assertions.assertEquals(id, result.getId());
        Assertions.assertEquals("John", result.getFirstName());
        Assertions.assertEquals("Doe", result.getLastName());
        Assertions.assertEquals("user@test.com", result.getEmail());
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
    public void updateWithNonExistingUserTest() {
        String id = "abc";
        var userDTO = getUserDTO(id);
        when(userRepository.findByIdAndIsDeletedFalse(id)).thenReturn(Optional.empty());
        var exception = Assertions.assertThrows(ResourceDoesNotExistException.class, () -> {
            userService.update(id, userDTO);
        });
        Assertions.assertEquals(String.format(ResourceDoesNotExistException.MESSAGE, USER_ENTITY, id), exception.getMessage());
    }

    @Test
    public void loadUserByEmailIdTest() {
        String email = "user@test.com";
        User user = getUser("abc");
        UserDTO userDTO = getUserDTO("abc");
        when(userRepository.findByEmailAndIsDeletedFalse(email)).thenReturn(Optional.of(user));
        when(userMapper.entityToDTO(user)).thenReturn(userDTO);
        var result = userService.loadUserByUsername(email);
        Assertions.assertInstanceOf(UserDTO.class, result);
        Assertions.assertEquals(email, result.getEmail());
    }

    @Test
    public void saveUserWithEmailAndPasswordTest() throws Exception{
        String email = "user@test.com";
        String password = "password";

        User user = getUser(null);
        user.setId(null);
        user.setFirstName(null);
        user.setLastName(null);

        UserDTO userDTO = getUserDTO("abc");

        when(userRepository.save(any())).thenReturn(user);
        when(userMapper.entityToDTO(any())).thenReturn(userDTO);
        doNothing().when(auditService).populateAuditFields(any());

        var result = userService.saveUser(email, password);
        Assertions.assertEquals(result.getId(), userDTO.getId());
        verify(auditService, times(1)).populateAuditFields(any());
    }

    @Test
    public void saveUserWithEmailAndNullPasswordTest() {
        String email = "user@test.com";
        String password = null;
        var exception = Assertions.assertThrows(Exception.class, () -> {
            validateParameters("saveUser", new Object[]{email, password});
        });
        Assertions.assertEquals("saveUser.encryptedPassword: Password cannot be null", exception.getMessage());
    }
    @Test
    public void saveUserWithNullEmailAndPasswordTest() {
        String email = null;
        String password = "password";
        var exception = Assertions.assertThrows(Exception.class, () -> {
            validateParameters("saveUser", new Object[]{email, password});
        });
        Assertions.assertEquals("saveUser.email: Email cannot be null", exception.getMessage());
    }

    @Test
    public void saveUserWithInvalidEmailAndPasswordTest() {
        String email = "invalid-email";
        String password = "password";
        var exception = Assertions.assertThrows(Exception.class, () -> {
            validateParameters("saveUser", new Object[]{email, password});
        });
        Assertions.assertEquals("saveUser.email: Provided invalid email", exception.getMessage());
    }

    @Test
    public void getUserByEmailTest() throws ResourceDoesNotExistException {
        String email = "user@test.com";
        String password = "password";
        User user = getUser("abc");
        when(userRepository.findByEmailAndIsDeletedFalse(email)).thenReturn(Optional.of(user));
        var result = userService.getUserByEmail(email);
        Assertions.assertInstanceOf(User.class, result);
        Assertions.assertEquals(email, result.getEmail());
    }

    @Test
    public void updateUserRoleByUserWithUserRole() throws Exception {
        var existingUser = getUser("1");
        var updatedUser = getUserDTO("2");
        updatedUser.setUserRole(UserRole.ADMIN);

        when(userProfileFacade.isUsersWithRole(UserRole.ADMIN)).thenReturn(false);
        when(userRepository.findByIdAndIsDeletedFalse("1")).thenReturn(Optional.of(existingUser));

        Assertions.assertThrows(UnprivilegedToModificationException.class, () -> userService.update("1", updatedUser));
    }

    @Test
    public void updateUserRoleByUserWithAdminRole() throws Exception {
        var existingUser = getUser("1");
        var updatedUser = getUserDTO("1");
        var newUser = getUser("1");

        updatedUser.setUserRole(UserRole.ADMIN);
        newUser.setUserRole(UserRole.ADMIN);

        when(userProfileFacade.isUsersWithRole(UserRole.ADMIN)).thenReturn(true);
        when(userRepository.findByIdAndIsDeletedFalse("1")).thenReturn(Optional.of(existingUser));
        when(userMapper.dtoToEntity(updatedUser)).thenReturn(newUser);
        when(userMapper.entityToDTO(any())).thenReturn(updatedUser);
        doNothing().when(auditService).populateAuditFields(any());

        var result = userService.update("1", updatedUser);
        Assertions.assertEquals(result.getId(), updatedUser.getId());
        verify(auditService, times(1)).populateAuditFields(any());
    }

    @Test
    public void updateWithInvalidTimeZone() throws Exception {
        var existingUser = getUser("1");
        var updatedUser = getUserDTO("1");
        var newUser = getUser("1");

        var timeZoneId = "invalidTimeZone";
        updatedUser.setTimeZoneId(timeZoneId);

        when(userProfileFacade.isUsersWithRole(UserRole.ADMIN)).thenReturn(false);
        when(userRepository.findByIdAndIsDeletedFalse("1")).thenReturn(Optional.of(existingUser));
        when(timeZoneService.isValidTImeZone(timeZoneId)).thenReturn(false);

        Assertions.assertThrows(InvalidFieldException.class, () -> userService.update("1", updatedUser));
    }

    @Test
    public void updateWithNullTimeZone() throws Exception {
        var existingUser = getUser("1");
        var updatedUser = getUserDTO("1");
        var newUser = getUser("1");
        existingUser.setTimeZoneId("invalidTimeZone");

        when(userProfileFacade.isUsersWithRole(UserRole.ADMIN)).thenReturn(false);
        when(userRepository.findByIdAndIsDeletedFalse("1")).thenReturn(Optional.of(existingUser));

        Assertions.assertThrows(InvalidFieldException.class, () -> userService.update("1", updatedUser));
    }

    private User getUser(String id) {
        var user = new User();
        user.setId(id);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("user@test.com");
        user.setUserRole(UserRole.USER);
        return user;
    }

    private UserDTO getUserDTO(String id) {
        var userDTO = new UserDTO();
        userDTO.setId(id);
        userDTO.setFirstName("John");
        userDTO.setLastName("Doe");
        userDTO.setEmail("user@test.com");
        userDTO.setUserRole(UserRole.USER);
        return userDTO;
    }

    private void validateParameters(String methodName, Object[] parameters) {
        try {
            Method method = UserService.class.getMethod(methodName, String.class, String.class);
            Set<ConstraintViolation<UserService>> violations = validator.forExecutables()
                    .validateParameters(userService, method, parameters);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Method not found: " + methodName, e);
        }
    }

}
