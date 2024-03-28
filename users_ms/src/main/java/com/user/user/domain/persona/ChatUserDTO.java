package com.user.user.domain.persona;

public record ChatUserDTO(String firstName, String lastName, String picture) {
    public ChatUserDTO(Persona persona) {
        this(persona.getFirstName(), persona.getLastName(), persona.getPicture());
    }
}