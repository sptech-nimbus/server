package com.user.user.domain.team;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.user.user.domain.athlete.Athlete;
import com.user.user.domain.coach.Coach;
import com.user.user.domain.persona.Pictured;

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
public class Team extends Pictured {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "team_id")
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "picture", nullable = true)
    private String picture;

    @Column(name = "local", nullable = true)
    private String local;
    
    @Column(name = "level", nullable = true)
    private Integer level;

    @ManyToOne(optional = false)
    @JoinColumn(name = "coach_id", referencedColumnName = "coach_id")
    private Coach coach;

    @JsonIgnore
    @OneToMany(mappedBy = "team")
    private List<Athlete> athletes;

    @Override
    public void setPicture(String picturePath) {
        this.picture = picturePath;
    }
}