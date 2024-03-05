package com.events.events.domains.game;

import java.time.LocalDateTime;
import java.util.List;

import com.events.events.domains.athleteHistoric.AthleteHistoric;

public record GameDTO(
        LocalDateTime dateTime,
        String local,
        String challenger,
        String challenged,
        List<AthleteHistoric> athletesHistorics) {

}