package com.user.user.models.team;

import java.util.List;

import com.user.user.models.athlete.Athlete;
import com.user.user.models.coach.Coach;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "team")
@Table(name = "team")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "team_id")
    private String id;

    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "picture", nullable = true)
    private String picture;

    @Column(name = "local", nullable = true)
    private String local;

    @ManyToOne(optional = false)
    @JoinColumn(name = "coach_id", referencedColumnName = "coach_id")
    private Coach coach;

    @OneToMany(mappedBy = "team")
    private List<Athlete> athletes;

    public Team(TeamDTO dto) {
        this.coach = dto.coach();
        this.setCategory(dto.category());
        this.setLocal(dto.local());
        this.setPicture(dto.picture());
    }
}