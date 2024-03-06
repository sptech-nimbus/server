package com.chat.chat.controllers;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.chat.chat.domains.message.ChatMessageDTO;
import com.chat.chat.domains.message.Message;
import com.chat.chat.domains.message.MessageDTO;
import com.chat.chat.domains.responseMessage.ResponseMessage;
import com.chat.chat.repositories.MessageRepository;

@SuppressWarnings("rawtypes")
@RestController
@RequestMapping("/message")
public class MessageController {
    @Autowired
    private MessageRepository repository;

    @Autowired
    private RestTemplate restTemplate;

    @PostMapping(consumes = "application/json")
    @CrossOrigin
    @SendTo("/topic/messages")
    @MessageMapping("/sendMessage")
    public ResponseEntity<ResponseMessage> registerMessage(@RequestBody MessageDTO message) {
        Message newMessage = new Message(message);
        if (newMessage.getDate() == null)
            newMessage.setDate(LocalDateTime.now());

        ResponseEntity<ResponseMessage> chatUserEntity = restTemplate
                .getForEntity("http://localhost:3000/users/ms-get-chat-user/" + newMessage.getUserId(),
                        ResponseMessage.class);

        ChatMessageDTO chatMessageDTO = new ChatMessageDTO(newMessage, chatUserEntity.getBody().getData());

        repository.save(newMessage);

        return ResponseEntity.ok(new ResponseMessage<ChatMessageDTO>(chatMessageDTO));
    }

    @GetMapping
    @CrossOrigin
    public ResponseEntity<List<Message>> findLastMessages() {
        List<Message> messages = repository.findAll();

        return ResponseEntity.ok(messages);
    }
}
