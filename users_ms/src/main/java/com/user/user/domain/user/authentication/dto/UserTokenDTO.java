package com.user.user.domain.user.authentication.dto;

import java.util.UUID;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserTokenDTO {
    private UUID userId;
    private String username;
    private String email;
    private String token;
    private UUID personaId;

    public UserTokenDTO(UUID id, UUID personaId, String token, String username) {
        this.userId = id;
        this.token = token;
        this.personaId = personaId;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UUID getPersonaId() {
        return personaId;
    }

    public void setPersonaId(UUID personaId) {
        this.personaId = personaId;
    }
}