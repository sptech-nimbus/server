package com.user.user.domain.persona;

import java.time.LocalDate;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.user.user.domain.user.User;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Persona extends Pictured {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String phone;
    private String picture;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @Override
    public void setPicture(String picturePath) {
        this.picture = picturePath;
    }

    @Override
    public String toString() {
        return "Persona { " + '\'' +
                "id=" + id + '\'' +
                "firstName=" + firstName + '\'' +
                "lastName=" + lastName + '\'' +
                "birthDate=" + birthDate + '\'' +
                "phone=" + phone + '\'' +
                "picture=" + picture + '\'' +
                "user=" + user + '\'' +
                "}";
    }
}