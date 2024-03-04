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

import com.chat.chat.domains.message.Message;
import com.chat.chat.domains.message.MessageDTO;
import com.chat.chat.domains.responseMessage.ResponseMessage;
import com.chat.chat.domains.user.User;
import com.chat.chat.repositories.MessageRepository;

@RestController
@RequestMapping("/message")
public class MessageController {
    @Autowired
    private MessageRepository repository;

    @Autowired
    private RestTemplate restTemplate;

    @SuppressWarnings("rawtypes")
    @PostMapping(consumes = "application/json")
    @CrossOrigin
    @SendTo("/topic/messages")
    @MessageMapping("/sendMessage")
    public ResponseEntity<ResponseMessage> registerMessage(@RequestBody MessageDTO message) {
        Message mensagem = new Message(message);
        if (mensagem.getDate() == null)
            mensagem.setDate(LocalDateTime.now());

        ResponseEntity<User> userEntity = restTemplate
                .getForEntity("http://localhost:3000/users/" + mensagem.getUserId(), User.class);

        System.out.println(userEntity);

        repository.save(mensagem);

        return ResponseEntity.ok(new ResponseMessage<Message>("Mensagem enviada", mensagem));
    }

    @GetMapping
    @CrossOrigin
    public ResponseEntity<List<Message>> findLastMessages() {
        List<Message> messages = repository.findAll();

        return ResponseEntity.ok(messages);
    }
}
