package com.user.user.models.coach;

import com.user.user.models.persona.Persona;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "coach_id")),
        @AttributeOverride(name = "firstName", column = @Column(name = "first_name", nullable = false)),
        @AttributeOverride(name = "lastName", column = @Column(name = "last_name", nullable = false)),
        @AttributeOverride(name = "birthDate", column = @Column(name = "birth_date", nullable = false)),
        @AttributeOverride(name = "phone", column = @Column(name = "phone", nullable = true)),
        @AttributeOverride(name = "picture", column = @Column(name = "picture", nullable = true))
})
@Entity(name = "coach")
@Table(name = "coach")
@Getter
@Setter
public class Coach extends Persona {
    public Coach(CoachDTO dto) {
        this.setFirstName(dto.firstName());
        this.setLastName(dto.lastName());
        this.setBirthDate(dto.birthDate());
        this.setPhone(dto.phone());
        this.setPicture(dto.picture());
    }
}
