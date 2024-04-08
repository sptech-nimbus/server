package com.user.user.domain.training;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.user.user.domain.athleteHistoric.AthleteHistoric;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Training {
    private UUID id;
    private LocalDateTime inicialDateTime;
    private LocalDateTime finalDateTime;
    private String local;
    private UUID team;
    private List<AthleteHistoric> athletesHistorics;

    @Override
    public String toString() {
        return "Training {" + '\'' +
                "id=" + id + '\'' +
                "inicialDateTime=" + inicialDateTime + '\'' +
                "finalDateTime=" + finalDateTime + '\'' +
                "local=" + local + '\'' +
                "team=" + team + '\'' +
                "athletesHistorics=" + athletesHistorics + '\'' +
                "}";
    }
}