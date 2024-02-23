package com.chat.chat.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chat.chat.domains.message.Message;

public interface MessageRepository extends JpaRepository<Message, String> {
	// @Query(value = "SELECT m.messageid as messageid, m.message as message, m.date
	// as date, u as user FROM Message m JOIN m.user u ORDER BY m.date DESC")
	// List<Message> findLastMessages();
}
