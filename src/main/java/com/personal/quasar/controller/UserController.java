package com.personal.quasar.controller;

import com.personal.quasar.common.exception.ImmutableFieldModificationException;
import com.personal.quasar.common.exception.InvalidFieldException;
import com.personal.quasar.common.exception.ResourceDoesNotExistException;
import com.personal.quasar.common.exception.UnprivilegedToModificationException;
import com.personal.quasar.model.dto.UserDTO;
import com.personal.quasar.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    UserService userService;
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> update(@PathVariable String id,@Validated @RequestBody UserDTO user)
            throws ImmutableFieldModificationException, ResourceDoesNotExistException,
            UnprivilegedToModificationException, InvalidFieldException {
        return ResponseEntity.ok(userService.update(id, user));
    }
}
