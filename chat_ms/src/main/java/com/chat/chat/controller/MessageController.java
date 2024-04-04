package com.chat.chat.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chat.chat.domain.message.ChatMessageDTO;
import com.chat.chat.domain.message.Message;
import com.chat.chat.domain.message.MessageDTO;
import com.chat.chat.domain.responseMessage.ResponseMessage;
import com.chat.chat.service.MessageService;

@RestController
@RequestMapping("/messages")
public class MessageController {
    @Autowired
    private MessageService service;

    @PostMapping
    @CrossOrigin
    @SendTo("/chat/{teamId}")
    @MessageMapping("/send-message/{teamId}")
    public ResponseEntity<ResponseMessage<ChatMessageDTO>> registerMessage(@RequestBody MessageDTO dto,
            @DestinationVariable UUID teamId) {
        return service.register(dto, teamId);
    }

    @GetMapping("{teamId}")
    public ResponseEntity<ResponseMessage<List<Message>>> getMessagesByTeamId(@PathVariable UUID teamId) {
        return service.getMessagesByTeamId(teamId);
    }
}