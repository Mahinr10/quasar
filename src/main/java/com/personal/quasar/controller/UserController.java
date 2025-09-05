package com.personal.quasar.controller;

import com.personal.quasar.exception.ImmutableFieldModificationException;
import com.personal.quasar.exception.ResourceDoesNotExistException;
import com.personal.quasar.model.dto.UserDTO;
import com.personal.quasar.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
            throws ImmutableFieldModificationException, ResourceDoesNotExistException {
        return ResponseEntity.ok(userService.update(id, user));
    }
}
