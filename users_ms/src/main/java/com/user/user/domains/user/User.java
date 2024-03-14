package com.user.user.domains.user;

import java.util.UUID;

import com.user.user.domains.athlete.Athlete;
import com.user.user.domains.coach.Coach;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
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
    private UUID id;

    @Column(name = "email", nullable = false, columnDefinition = "VARCHAR(255) COLLATE utf8_bin", unique = true)
    private String email;

    @Column(name = "password", nullable = false, columnDefinition = "VARCHAR(255) COLLATE utf8_bin")
    private String password;

    @OneToOne(mappedBy = "user")
    private Coach coach;

    @OneToOne(mappedBy = "user")
    private Athlete athlete;

    public User(UserDTO dto) {
        this.email = dto.email();
        this.password = dto.password();
    }

    @Override
    public String toString() {
        return "User {" + '\'' +
                "id=" + id + '\'' +
                "email=" + email + '\'' +
                "password=" + password + '\'' +
                "coach=" + coach + '\'' +
                "athlete=" + athlete + '\'' +
                "}";
    }

    
}