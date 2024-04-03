package com.user.user.domain.user.authentication.dto;

import java.util.UUID;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserTokenDTO {
    private UUID userId;
    private String nome;
    private String email;
    private String token;

    public UserTokenDTO(UUID id, String token) {
        this.userId = id;
        this.token = token;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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
}