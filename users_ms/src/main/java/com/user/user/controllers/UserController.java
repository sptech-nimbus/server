package com.user.user.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.user.user.models.responseMessage.ResponseMessage;
import com.user.user.models.user.User;
import com.user.user.models.user.UserDTO;
import com.user.user.repositories.UserRepository;
import com.user.user.services.UserService;

@RestController
@RequestMapping("users")
public class UserController {
    @Autowired
    private UserRepository repo;

    @Autowired
    private UserService service;

    @SuppressWarnings("rawtypes")
    @PostMapping
    public ResponseEntity<ResponseMessage> registerUser(@RequestBody UserDTO dto) {
        if (!service.checkAllUserCredencials(dto)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseMessage("Verifique suas credenciais"));
        }

        if (repo.findByEmail(dto.email()).size() > 0) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseMessage("Email já utilizado"));
        }

        User newUser = new User(dto);
        repo.save(newUser);
        return ResponseEntity
                .ok(new ResponseMessage<String>("Cadastro realizado", "Cadastro realizado", newUser.getId()));
    }

    @SuppressWarnings("rawtypes")
    @PostMapping("login")
    public ResponseEntity<ResponseMessage> login(@RequestBody UserDTO dto) {
        User userFound = repo.findByEmailAndPassword(dto.email(), dto.password());

        if (userFound == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage("Email ou senha incorretos"));
        }

        return ResponseEntity
                .ok(new ResponseMessage<String>("Login realizado", "Login realizado", userFound.getId()));
    }
}