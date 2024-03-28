package com.user.user.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.user.user.domain.responseMessage.ResponseMessage;
import com.user.user.domain.user.ChangePasswordDTO;
import com.user.user.domain.user.ChangePasswordRequestDTO;
import com.user.user.domain.user.UserDTO;
import com.user.user.exception.ResourceNotFoundException;
import com.user.user.service.PersonaService;
import com.user.user.service.UserService;

@SuppressWarnings("rawtypes")
@RestController
@RequestMapping("users")
public class UserController {
    private final UserService service;

    private final PersonaService personaService;

    public UserController(UserService userService, PersonaService personaService) {
        this.personaService = personaService;
        this.service = userService;
    }

    // POST
    @PostMapping
    @CrossOrigin
    public ResponseEntity<ResponseMessage> registerUser(@RequestBody UserDTO dto) {
        return service.register(dto);
    }

    @PostMapping("login")
    @CrossOrigin
    public ResponseEntity<ResponseMessage> login(@RequestBody UserDTO dto) {
        try {
            return service.login(dto);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ResponseMessage("Usuário não encontrado"));
        }
    }

    @PostMapping("change-password-request")
    public ResponseEntity<ResponseMessage> changePasswordRequest(@RequestBody ChangePasswordRequestDTO dto) {
        try {
            return service.changePasswordRequest(dto);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ResponseMessage(e.getMessage()));
        }
    }

    // GET
    @GetMapping("/{id}")
    @CrossOrigin
    public ResponseEntity<ResponseMessage> getUserById(@PathVariable UUID id) {
        return personaService.getPersonaByUserId(id);
    }

    @GetMapping("/ms-get-chat-user/{id}")
    public ResponseEntity<ResponseMessage> getChatUserByUserId(@PathVariable UUID id) {
        return personaService.getChatUserByUserId(id);
    }

    // PATCH
    @PatchMapping("/change-password/{id}")
    @CrossOrigin
    public ResponseEntity<ResponseMessage> changePassword(@PathVariable UUID id, @RequestBody ChangePasswordDTO dto) {
        try {
            return service.changePassword(id, dto);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ResponseMessage("Usuário não encontrado"));
        }
    }

    // DELETE
    @DeleteMapping("/{id}")
    @CrossOrigin
    public ResponseEntity<ResponseMessage> deleteUser(@PathVariable UUID id, @RequestBody String password) {
        try {
            return service.deleteUser(id, password);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ResponseMessage("Usuário não encontrado"));
        }
    }
}