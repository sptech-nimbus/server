package com.user.user.domain.athleteHistoric;

import com.user.user.domain.athlete.Athlete;
import com.user.user.domain.game.Game;
import com.user.user.domain.training.Training;

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
                Athlete athlete) {
}
