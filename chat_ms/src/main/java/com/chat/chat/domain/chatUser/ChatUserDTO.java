package com.chat.chat.domain.chatUser;

public record ChatUserDTO(String firstName, String lastName, String picture) {

    @Override
    public String toString() {
        return "ChatUserDTO []";
    }
}



