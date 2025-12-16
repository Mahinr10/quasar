package com.personal.quasar.controller;

import com.personal.quasar.common.exception.ImmutableFieldModificationException;
import com.personal.quasar.common.exception.InvalidFieldException;
import com.personal.quasar.common.exception.ResourceDoesNotExistException;
import com.personal.quasar.common.exception.UnprivilegedToModificationException;
import com.personal.quasar.model.dto.UserDTO;
import com.personal.quasar.service.impl.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@Slf4j
public class UserController {

    @Autowired
    UserService userService;
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> update(@PathVariable String id,@Validated @RequestBody UserDTO user)
            throws ImmutableFieldModificationException, ResourceDoesNotExistException,
            UnprivilegedToModificationException, InvalidFieldException {
        log.info("started update method with id - {} and payload - {}", id, user);
        return ResponseEntity.ok(userService.update(id, user));
    }

    @GetMapping
    public ResponseEntity<UserDTO> getActiveUser() throws ResourceDoesNotExistException {
        log.info("started getActiveUser method");
        return ResponseEntity.ok(userService.get());
    }
}
