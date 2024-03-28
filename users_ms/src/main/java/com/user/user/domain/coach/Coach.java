package com.user.user.domain.coach;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.user.user.domain.persona.Persona;
import com.user.user.domain.team.Team;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "coach_id")),
        @AttributeOverride(name = "firstName", column = @Column(name = "first_name", nullable = false)),
        @AttributeOverride(name = "lastName", column = @Column(name = "last_name", nullable = false)),
        @AttributeOverride(name = "birthDate", column = @Column(name = "birth_date", nullable = false)),
        @AttributeOverride(name = "phone", column = @Column(name = "phone", nullable = true)),
        @AttributeOverride(name = "picture", column = @Column(name = "picture", nullable = true)),
        @AttributeOverride(name = "user", column = @Column(name = "user_id", nullable = true))
})
@Entity(name = "coach")
@Table(name = "coach")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Coach extends Persona {
    @JsonIgnore
    @OneToMany(mappedBy = "coach")
    private List<Team> teams;
}