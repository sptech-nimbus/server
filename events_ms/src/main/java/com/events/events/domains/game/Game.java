package com.events.events.domains.game;

import java.time.LocalDateTime;
import java.util.List;

import com.events.events.domains.athleteHistoric.AthleteHistoric;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity(name = "game")
@Table(name = "game")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "game_id")
    private String id;

    @Column(name = "confirmed", columnDefinition = "boolean default false")
    private Boolean confirmed;

    @Column(name = "date_time", nullable = false)
    private LocalDateTime dateTime;

    @Column(name = "local")
    private String local;

    @Column(name = "callenger_id", nullable = false)
    private String challenger;

    @Column(name = "callenger_id", nullable = false)
    private String challenged;

    @OneToMany(mappedBy = "game")
    private List<AthleteHistoric> athletesHistorics;
}