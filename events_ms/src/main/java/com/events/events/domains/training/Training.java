package com.events.events.domains.training;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.events.events.domains.athleteHistoric.AthleteHistoric;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity(name = "training")
@Table(name = "training")
public class Training {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "training_id")
    private UUID id;

    @Column(name = "date_time")
    private LocalDateTime dateTime;

    @Column(name = "local")
    private String local;

    @Column(name = "team_id")
    private String team;

    @OneToMany(mappedBy = "training")
    private List<AthleteHistoric> athletesHistorics;
}