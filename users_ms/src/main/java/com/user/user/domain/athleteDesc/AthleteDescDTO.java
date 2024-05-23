package com.user.user.domain.athleteDesc;

import com.user.user.domain.athlete.Athlete;

public record AthleteDescDTO(
                Double weight, Double height,
                String position, Athlete athlete,
                String address, Integer number) {
}