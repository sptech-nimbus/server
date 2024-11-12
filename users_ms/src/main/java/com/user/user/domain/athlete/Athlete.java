package com.user.user.domain.athlete;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.user.user.domain.athleteDesc.AthleteDesc;
import com.user.user.domain.athleteHistoric.AthleteHistoric;
import com.user.user.domain.injury.Injury;
import com.user.user.domain.persona.Persona;
import com.user.user.domain.team.Team;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@AllArgsConstructor
@NoArgsConstructor
public class Athlete extends Persona {
        @Column(name = "category", nullable = false)
        private String category;

        @Column(name = "is_starting", nullable = false)
        private Boolean isStarting;

        @JsonIgnore
        @ManyToOne
        @JoinColumn(name = "team_id", referencedColumnName = "team_id")
        private Team team;

        @OneToMany(mappedBy = "athlete")
        private List<Injury> injuries;

        @JsonIgnore
        @OneToMany(mappedBy = "athlete")
        private List<AthleteHistoric> athleteHistorics;

        @OneToOne(mappedBy = "athlete")
        private AthleteDesc athleteDesc;
}