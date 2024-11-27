package com.user.user.controller;

import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.user.user.domain.persona.ChatUserDTO;
import com.user.user.domain.persona.NewUserDTO;
import com.user.user.domain.persona.Persona;
import com.user.user.domain.responseMessage.ResponseMessage;
import com.user.user.domain.user.ChangePasswordDTO;
import com.user.user.domain.user.ChangePasswordRequestDTO;
import com.user.user.domain.user.UserDTO;
import com.user.user.domain.user.authentication.dto.UserTokenDTO;
import com.user.user.exception.ResourceNotFoundException;
import com.user.user.service.PersonaService;
import com.user.user.service.UserService;

@RestController
@RequestMapping(path = "users", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE, MediaType.ALL_VALUE })
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    private final PersonaService personaService;

    // POST
    @PostMapping
    public ResponseEntity<ResponseMessage<NewUserDTO>> registerUser(@RequestBody UserDTO dto) {
        return service.register(dto);
    }

    @PostMapping("login")
    public ResponseEntity<ResponseMessage<UserTokenDTO>> login(@RequestBody UserDTO dto) {
        try {
            return service.login(dto);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ResponseMessage<>("Usuário não encontrado"));
        }
    }

    @PostMapping("change-password-request")
    public ResponseEntity<ResponseMessage<?>> changePasswordRequest(@RequestBody ChangePasswordRequestDTO dto) {
        try {
            return service.changePasswordRequest(dto);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ResponseMessage<>(e.getMessage()));
        }
    }

    // GET
    @GetMapping("/{id}")
    public ResponseEntity<ResponseMessage<Persona>> getUserById(@PathVariable UUID id) {
        return personaService.getPersonaByUserId(id);
    }

    @GetMapping("/ms-get-chat-user/{id}")
    public ResponseEntity<ChatUserDTO> getChatUserByUserId(@PathVariable UUID id) {
        return personaService.getChatUserByUserId(id);
    }

    // PATCH
    @PatchMapping("/change-password/{id}")
    public ResponseEntity<ResponseMessage<?>> changePassword(@PathVariable UUID id,
                                                             @RequestBody ChangePasswordDTO dto) {
        try {
            return service.changePassword(id, dto);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ResponseMessage<>("Usuário não encontrado"));
        }
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseMessage<?>> deleteUser(@PathVariable UUID id, @RequestBody String password) {
        try {
            return service.deleteUser(id, password);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ResponseMessage<>("Usuário não encontrado"));
        }
    }
}