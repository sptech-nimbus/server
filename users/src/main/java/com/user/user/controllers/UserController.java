package com.user.user.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping
    public ResponseMessage<User> registerUser(@RequestBody UserDTO dto) {
        User newUser = new User(dto);
        repo.save(newUser);

        return new ResponseMessage<User>(newUser, 200);
    }

    @GetMapping
    public ResponseMessage<List<UserRes>> getAllUsers() {
        return new ResponseMessage<List<UserRes>>(service.getUserResList(repo.findAll()), 200);
    }
}
