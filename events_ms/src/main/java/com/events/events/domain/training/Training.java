package com.events.events.domain.training;

import java.time.LocalDateTime;
import java.util.UUID;

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

@Entity(name = "training")
@Table(name = "training")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Training {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "training_id")
    private UUID id;

    @Column(name = "inicial_date_time")
    private LocalDateTime inicialDateTime;

    @Column(name = "final_date_time")
    private LocalDateTime finalDateTime;

    @Column(name = "local")
    private String local;

    @Column(name = "team_id")
    private UUID team;

    public Training(TrainingDTO dto) {
        this.setFinalDateTime(dto.finalDateTime());
        this.setInicialDateTime(dto.inicialDateTime());
        this.setLocal(dto.local());
        this.setTeam(dto.teamId());
    }

    @Override
    public String toString() {
        return "Training {" + '\'' +
                "id=" + id + '\'' +
                "inicialDateTime=" + inicialDateTime + '\'' +
                "finalDateTime=" + finalDateTime + '\'' +
                "local=" + local + '\'' +
                "team=" + team + '\'' +
                "}";
    }
}