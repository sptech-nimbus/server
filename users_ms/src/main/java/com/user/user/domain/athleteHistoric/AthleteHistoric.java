package com.user.user.domain.athleteHistoric;

import java.util.UUID;

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
import lombok.ToString;

@Entity(name = "athlete_historic")
@Table(name = "athlete_historic")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AthleteHistoric {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "athlete_historic_id")
    private UUID id;

    @Column(name = "observations")
    private String observations;

    @Column(name = "off_rebounds")
    private Integer offRebounds;

    @Column(name = "def_rebounds")
    private Integer defRebounds;

    @Column(name = "blocks")
    private Integer blocks;

    @Column(name = "fouls")
    private Integer fouls;

    @Column(name = "turnovers")
    private Integer turnovers;

    @Column(name = "minutes")
    private Double minutes;

    @Column(name = "assists")
    private Integer assists;

    @Column(name = "free_throw_converted")
    private Integer freeThrowConverted;

    @Column(name = "free_throw_attemped")
    private Integer freeThrowAttemped;

    @Column(name = "steals")
    private Integer steals;

    @Column(name = "three_points_converted")
    private Integer threePointsConverted;

    @Column(name = "three_points_attemped")
    private Integer threePointsAttemped;

    @Column(name = "two_points_converted")
    private Integer twoPointsConverted;

    @Column(name = "two_points_attemped")
    private Integer twoPointsAttemped;

    @ManyToOne
    @JoinColumn(name = "athlete_id", referencedColumnName = "athlete_id")
    private Athlete athlete;

    @Column(name = "game_id")
    private UUID gameId;

    @Column(name = "training_id")
    private UUID trainingId;
}