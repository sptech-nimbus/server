package com.user.user.domain.game;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.user.user.domain.athleteHistoric.AthleteHistoric;
import com.user.user.domain.gameResult.GameResult;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Game {
    private UUID id;
    private Boolean confirmed;
    private LocalDateTime inicialDateTime;
    private LocalDateTime finalDateTime;
    private String local;
    private UUID challenger;
    private UUID challenged;
    private List<AthleteHistoric> athletesHistorics;
    private GameResult gameResult;

    @Override
    public String toString() {
        return "Game {" + '\'' +
                "id=" + id + '\'' +
                "confirmed=" + confirmed + '\'' +
                "inicialDateTime=" + inicialDateTime + '\'' +
                "finalDateTime=" + finalDateTime + '\'' +
                "local=" + local + '\'' +
                "challenger=" + challenger + '\'' +
                "challenged=" + challenged + '\'' +
                "}";
    }
}