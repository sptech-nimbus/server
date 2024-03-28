package com.chat.chat.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.chat.chat.domain.message.Message;

public interface MessageRepository extends MongoRepository<Message, UUID> {
    List<Message> getMessagesByTeamId(UUID teamId);
}