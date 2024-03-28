package com.events.events.domain.athleteHistoric;

import java.util.UUID;

import com.events.events.domain.game.Game;
import com.events.events.domain.training.Training;

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
                Integer freeThrowAttemped,
                Integer steals,
                Integer threePointsConverted,
                Integer threePointsAttemped,
                Integer twoPointsConverted,
                Integer twoPointsAttemped,
                Game game,
                Training training,
                UUID athleteId) {
}
