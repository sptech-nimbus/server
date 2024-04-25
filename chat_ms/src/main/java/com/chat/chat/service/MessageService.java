package com.chat.chat.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.chat.chat.domain.chatUser.ChatUserDTO;
import com.chat.chat.domain.message.ChatMessageDTO;
import com.chat.chat.domain.message.Message;
import com.chat.chat.domain.message.MessageDTO;
import com.chat.chat.domain.responseMessage.ResponseMessage;
import com.chat.chat.exception.ResourceNotFoundException;
import com.chat.chat.repository.MessageRepository;

@Service
public class MessageService {
    private final MessageRepository repo;
    private final RestTemplate restTemplate;

    public MessageService(MessageRepository repo, RestTemplate restTemplate) {
        this.repo = repo;
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<ResponseMessage<ChatMessageDTO>> register(MessageDTO dto, UUID teamId) {
        Message newMessage = new Message(dto, teamId);

        if (newMessage.getDate() == null)
            newMessage.setDate(LocalDateTime.now());

        ChatUserDTO chatUser;

        try {
            chatUser = restTemplate.getForEntity("http://localhost:3000/users/ms-get-chat-user/" + dto.userId(),
                    ChatUserDTO.class).getBody();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ResponseMessage<>(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(new ResponseMessage<>("Serviço de usuários fora do ar no momento", e.getMessage()));
        }

        ChatMessageDTO chatMessageDTO = new ChatMessageDTO(newMessage, chatUser);

        try {
            repo.save(newMessage);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(new ResponseMessage<>("Erro ao enviar mensagem", e.getMessage()));
        }

        return ResponseEntity.status(200).body(new ResponseMessage<ChatMessageDTO>(chatMessageDTO));
    }

    public ResponseEntity<ResponseMessage<List<Message>>> getMessagesByTeamId(UUID teamId) {
        List<Message> messages = repo.getMessagesByTeamId(teamId);

        return ResponseEntity.status(200).body(new ResponseMessage<List<Message>>(messages));
    }
}