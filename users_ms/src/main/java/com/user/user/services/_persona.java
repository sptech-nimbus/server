package com.user.user.services;

import org.springframework.http.ResponseEntity;

import com.user.user.domains.responseMessage.ResponseMessage;

@SuppressWarnings("rawtypes")
public interface _persona<T> {
    ResponseEntity<ResponseMessage> putPersona(String id, T dto);
}