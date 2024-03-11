package com.user.user.services;

import java.util.UUID;

import org.springframework.http.ResponseEntity;

import com.user.user.domains.responseMessage.ResponseMessage;

@SuppressWarnings("rawtypes")
public interface _persona<T> {
    ResponseEntity<ResponseMessage> putPersona(UUID id, T dto);
}