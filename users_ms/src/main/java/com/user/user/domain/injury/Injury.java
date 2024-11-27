package com.user.user.domain.injury;

import java.time.LocalDate;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.user.user.domain.athlete.Athlete;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "injury")
@Table(name = "injury")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Injury {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "injury_id")
    private UUID id;

    @Column(name = "type")
    private String type;

    @Column(name = "inicial_date")
    private LocalDate inicialDate;

    @Column(name = "final_date")
    private LocalDate finalDate;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "athlete_id", referencedColumnName = "athlete_id")
    private Athlete athlete;
}