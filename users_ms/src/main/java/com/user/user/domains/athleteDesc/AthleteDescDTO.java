package com.user.user.domains.athleteDesc;

import com.user.user.domains.athlete.Athlete;

public record AthleteDescDTO(
        Double weight, Double height,
        String position, Athlete athelete) {

}
