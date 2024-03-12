package com.chat.chat.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chat.chat.domains.message.MessageDTO;
import com.chat.chat.domains.responseMessage.ResponseMessage;
import com.chat.chat.services.MessageService;

@SuppressWarnings("rawtypes")
@RestController
@RequestMapping("/message")
public class MessageController {
    @Autowired
    private MessageService service;

    @PostMapping(consumes = "application/json")
    @CrossOrigin
    @SendTo("/topic/{teamId}")
    @MessageMapping("/send-message/{teamId}")
    public ResponseEntity<ResponseMessage> registerMessage(@RequestBody MessageDTO dto,
            @DestinationVariable String teamId) {
        return service.register(dto);
    }
}