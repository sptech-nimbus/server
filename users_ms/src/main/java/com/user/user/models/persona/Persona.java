package com.user.user.models.persona;

import java.time.LocalDate;

import com.user.user.models.user.User;

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
public class Persona {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String phone;
    private Long picture;

    @OneToOne(optional = true)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;
}
