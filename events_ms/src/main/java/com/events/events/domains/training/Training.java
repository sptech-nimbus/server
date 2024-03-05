package com.events.events.domains.training;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity(name = "training")
@Table(name = "training")
public class Training {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "date_time")
    private LocalDateTime dateTime;

    @Column(name = "local")
    private String local;

    @Column(name = "team_id")
    private String team;
}
