package com.chat.chat.domains.chatUser;

public record ChatUserDTO(String firstName, String lastName, String picture) {

    @Override
    public String toString() {
        return "ChatUserDTO []";
    }
}



