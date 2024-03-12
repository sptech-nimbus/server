package com.chat.chat.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.chat.chat.domains.message.Message;

public interface MessageRepository extends MongoRepository<Message, UUID> {
    List<Message> getMessagesByTeamId(UUID teamId);
}