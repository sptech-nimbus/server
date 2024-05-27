package com.events.events.domain.athleteHistoric;

import java.util.UUID;

import com.events.events.domain.athlete.Athlete;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AthleteHistoric {
    private UUID id;
    private Athlete athlete;
    private String observations;
    private Integer offRebounds;
    private Integer defRebounds;
    private Integer blocks;
    private Integer fouls;
    private Integer turnovers;
    private Double minutes;
    private Integer assists;
    private Integer freeThrowConverted;
    private Integer freeThrowAttemped;
    private Integer steals;
    private Integer threePointsConverted;
    private Integer threePointsAttemped;
    private Integer twoPointsConverted;
    private Integer twoPointsAttemped;
    private UUID gameId;
    private UUID trainingId;

    @Override
    public String toString() {
        return "AthleteHistoric {" + '\'' +
                "id=" + id + '\'' +
                "athlete=" + athlete + '\'' +
                "observations=" + observations + '\'' +
                "offRebounds=" + offRebounds + '\'' +
                "defRebounds=" + defRebounds + '\'' +
                "blocks=" + blocks + '\'' +
                "fouls=" + fouls + '\'' +
                "turnovers=" + turnovers + '\'' +
                "minutes=" + minutes + '\'' +
                "assists=" + assists + '\'' +
                "freeThrowConverted=" + freeThrowConverted + '\'' +
                "freeThrowAttemped=" + freeThrowAttemped + '\'' +
                "steals=" + steals + '\'' +
                "threePointsConverted=" + threePointsConverted + '\'' +
                "threePointsAttemped=" + threePointsAttemped + '\'' +
                "twoPointsConverted=" + twoPointsConverted + '\'' +
                "twoPointsAttemped=" + twoPointsAttemped + '\'' +
                "training=" + trainingId + '\'' +
                "}";
    }
}