package com.user.user.domain.gameResult;

import java.util.List;

import com.user.user.domain.athleteHistoric.AthleteHistoric;

public record InGameForecastDTO(
                List<AthleteHistoric> challengerHistorics,
                List<AthleteHistoric> challengedHistorics) {
}