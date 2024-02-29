package com.user.user.models.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "user")
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id")
    private String id;

    @Column(name = "email", nullable = false, columnDefinition = "COLLATE utf8_bin", unique = true)
    private String email;

    @Column(name = "password", nullable = false, columnDefinition = "COLLATE utf8_bin")
    private String password;

    public User(UserDTO dto) {
        this.email = dto.email();
        this.password = dto.password();
    }
}