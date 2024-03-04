package com.user.user.models.athleteDesc;

import com.user.user.models.athlete.Athlete;

public record AthleteDescDTO(
        Double weight, Double height,
        String position, Athlete athelete) {

}
