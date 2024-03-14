package com.events.events.domains.athleteHistoric;

import com.events.events.domains.game.Game;
import com.events.events.domains.training.Training;

public record AthleteHistoricDTO(
        String observations,
        Integer offRebounds,
        Integer defRebounds,
        Integer blocks,
        Integer fouls,
        Integer turnovers,
        Double minutes,
        Integer assists,
        Integer freeThrowConverted,
        Integer freeThowAttemped,
        Integer steals,
        Integer threePointsConverted,
        Integer threePointsAttemped,
        Integer twoPointsConverted,
        Integer twoPointsAttemped,
        Game game,
        Training training) {
}
