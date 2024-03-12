package com.chat.chat.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    public ResponseEntity<ResponseMessage> register(MessageDTO dto, UUID teamId) {
        Message newMessage = new Message(dto, teamId);
        if (newMessage.getDate() == null)
            newMessage.setDate(LocalDateTime.now());

        ResponseEntity<ResponseMessage> chatUserEntity;

        try {
            chatUserEntity = restTemplate
                    .getForEntity("http://localhost:3000/users/ms-get-chat-user/" + newMessage.getUserId(),
                            ResponseMessage.class);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseMessage("Serviço de usuários não disponível no momento", e.getMessage()));
        }

        ChatMessageDTO chatMessageDTO = new ChatMessageDTO(newMessage, chatUserEntity.getBody().getData());

        try {
            repo.save(newMessage);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseMessage("Erro ao enviar mensagem", e.getMessage()));
        }

        return ResponseEntity.ok(new ResponseMessage<ChatMessageDTO>(chatMessageDTO));
    }

    public ResponseEntity<ResponseMessage> getMessagesByTeamId(UUID teamId) {
        List<Message> messages = repo.getMessagesByTeamId(teamId);

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage<List<Message>>(messages));
    }
}