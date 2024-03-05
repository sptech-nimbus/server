package com.user.user.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.user.user.domains.responseMessage.ResponseMessage;
import com.user.user.domains.user.ChangePasswordDTO;
import com.user.user.domains.user.UserDTO;
import com.user.user.services.PersonaService;
import com.user.user.services.UserService;

@SuppressWarnings("rawtypes")
@RestController
@RequestMapping("users")
public class UserController {
    @Autowired
    private UserService service;

    @Autowired
    private PersonaService personaService;

    @PostMapping
    public ResponseEntity<ResponseMessage> registerUser(@RequestBody UserDTO dto) {
        return service.register(dto);
    }

    @PostMapping("login")
    public ResponseEntity<ResponseMessage> login(@RequestBody UserDTO dto) {
        return service.login(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseMessage> getUserById(@PathVariable String id) {
        return personaService.getPersonaByUserId(id);
    }

    @PutMapping("/changePassword/{id}")
    public ResponseEntity<ResponseMessage> changePassword(@PathVariable String id, @RequestBody ChangePasswordDTO dto) {
        return service.changePassword(id, dto);
    }
}