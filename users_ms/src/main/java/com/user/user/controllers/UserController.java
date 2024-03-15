package com.user.user.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.user.user.domains.responseMessage.ResponseMessage;
import com.user.user.domains.user.ChangePasswordDTO;
import com.user.user.domains.user.UserDTO;
import com.user.user.exceptions.ResourceNotFoundException;
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

    // POST
    @PostMapping
    public ResponseEntity<ResponseMessage> registerUser(@RequestBody UserDTO dto) {
        return service.register(dto);
    }

    @PostMapping("login")
    public ResponseEntity<ResponseMessage> login(@RequestBody UserDTO dto) {
        try {
            return service.login(dto);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ResponseMessage("Usuário não encontrado"));
        }
    }

    // GET
    @GetMapping("/{id}")
    public ResponseEntity<ResponseMessage> getUserById(@PathVariable UUID id) {
        return personaService.getPersonaByUserId(id);
    }

    @GetMapping("/ms-get-chat-user/{id}")
    public ResponseEntity<ResponseMessage> getChatUserByUserId(@PathVariable UUID id) {
        return personaService.getChatUserByUserId(id);
    }

    // PATCH
    @PatchMapping("/change-password/{id}")
    public ResponseEntity<ResponseMessage> changePassword(@PathVariable UUID id, @RequestBody ChangePasswordDTO dto) {
        try {
            return service.changePassword(id, dto);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ResponseMessage("Usuário não encontrado"));
        }
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseMessage> deleteUser(@PathVariable UUID id, @RequestBody String password) {
        try {
            return service.deleteUser(id, password);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ResponseMessage("Usuário não encontrado"));
        }
    }
}