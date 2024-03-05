ackage com.chat.chat.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chat.chat.domains.message.Message;

public interface MessageRepository extends JpaRepository<Message, String> {
	
}