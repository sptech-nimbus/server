package com.user.user.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.user.user.models.responseMessage.ResponseMessage;
import com.user.user.models.user.UserDTO;
import com.user.user.services.UserService;

@SuppressWarnings("rawtypes")
@RestController
@RequestMapping("users")
public class UserController {
    @Autowired
    private UserService service;

    @PostMapping
    public ResponseEntity<ResponseMessage> registerUser(@RequestBody UserDTO dto) {
        return service.register(dto);
    }

    @PostMapping("login")
    public ResponseEntity<ResponseMessage> login(@RequestBody UserDTO dto) {
        return service.login(dto);
    }
}