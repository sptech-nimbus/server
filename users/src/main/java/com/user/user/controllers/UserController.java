package com.user.user.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.user.user.models.user.User;
import com.user.user.models.user.UserDTO;
import com.user.user.models.user.User.UserRes;
import com.user.user.repositories.UserRepository;
import com.user.user.services.UserService;

import models.ResponseMessage;

@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserRepository repo;

    @Autowired
    private UserService service;

    @SuppressWarnings("rawtypes")
    @PostMapping
    public ResponseEntity<ResponseMessage> registerUser(@RequestBody UserDTO dto) {
        if (!service.checkAllUserCredencials(dto)) {
            return ResponseEntity.status(403).body(new ResponseMessage("Credenciais incorretas"));
        }

        if (repo.findUserByEmail(dto.email()).size() > 0) {
            return ResponseEntity.status(403).body(new ResponseMessage("Email já utilizado"));
        }

        User newUser = new User(dto);
        repo.save(newUser);
        return ResponseEntity.ok(new ResponseMessage<User>(newUser));
    }

    @SuppressWarnings("rawtypes")
    @GetMapping
    public ResponseEntity<ResponseMessage> getAllUsers() {
        return ResponseEntity.ok(new ResponseMessage<List<UserRes>>(service.getUserResList(repo.findAll())));
    }
}
