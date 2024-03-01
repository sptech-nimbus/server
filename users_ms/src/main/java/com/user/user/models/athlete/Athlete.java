package com.user.user.models.athlete;

import com.user.user.models.persona.Persona;
import com.user.user.models.team.Team;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@AttributeOverrides({
                @AttributeOverride(name = "id", column = @Column(name = "athlete_id")),
                @AttributeOverride(name = "firstName", column = @Column(name = "first_name", nullable = false)),
                @AttributeOverride(name = "lastName", column = @Column(name = "last_name", nullable = false)),
                @AttributeOverride(name = "birthDate", column = @Column(name = "birth_date", nullable = false)),
                @AttributeOverride(name = "phone", column = @Column(name = "phone", nullable = true)),
                @AttributeOverride(name = "picture", column = @Column(name = "picture", nullable = true))
})
@Entity(name = "athlete")
@Table(name = "athlete")
@Getter
@Setter
public class Athlete extends Persona {
        @Column(name = "category", nullable = false)
        private String category;

        @Column(name = "is_starting", nullable = false, columnDefinition = "boolean default 0")
        private Boolean starting;

        @ManyToOne
        @JoinColumn(name="team_id", referencedColumnName = "team_id")
        private Team team;
}