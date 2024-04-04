package com.user.user.service;

import java.util.UUID;

import org.springframework.http.ResponseEntity;

import com.user.user.domain.responseMessage.ResponseMessage;

public interface _persona<T> {
    ResponseEntity<ResponseMessage<?>> putPersona(UUID id, T dto);
}