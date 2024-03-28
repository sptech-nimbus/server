package com.chat.chat.domain.message;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection = "messages")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "messageId")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID messageId;

    private String message;

    private LocalDateTime date;

    private UUID userId;

    private UUID teamId;

    public Message(MessageDTO dto, UUID teamId) {
        this.message = dto.message();
        this.userId = dto.userId();
        this.teamId = teamId;
    }

    @Override
    public String toString() {
        return "Messages{" +
                "messageId=" + messageId + '\'' +
                "message=" + message + '\'' +
                "date=" + date + '\'' +
                "userId=" + userId + '\'' +
                "teamId=" + teamId +
                "}";
    }

    
}
