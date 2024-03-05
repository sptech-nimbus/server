package com.events.events.domains.athleteHistoric;

public record AtheteHistoricDTO(
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
        Integer twoPointsAttemped) {
}
