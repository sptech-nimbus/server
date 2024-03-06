package com.chat.chat.services;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.chat.chat.domains.message.ChatMessageDTO;
import com.chat.chat.domains.message.Message;
import com.chat.chat.domains.message.MessageDTO;
import com.chat.chat.domains.responseMessage.ResponseMessage;
import com.chat.chat.repositories.MessageRepository;

@SuppressWarnings("rawtypes")
@Service
public class MessageService {
    @Autowired
    private MessageRepository repo;

    @Autowired
    private RestTemplate restTemplate;

    public ResponseEntity<ResponseMessage> register(MessageDTO dto) {
        Message newMessage = new Message(dto);
        if (newMessage.getDate() == null)
            newMessage.setDate(LocalDateTime.now());

        ResponseEntity<ResponseMessage> chatUserEntity = restTemplate
                .getForEntity("http://localhost:3000/users/ms-get-chat-user/" + newMessage.getUserId(),
                        ResponseMessage.class);

        ChatMessageDTO chatMessageDTO = new ChatMessageDTO(newMessage, chatUserEntity.getBody().getData());

        repo.save(newMessage);

        return ResponseEntity.ok(new ResponseMessage<ChatMessageDTO>(chatMessageDTO));
    }
}