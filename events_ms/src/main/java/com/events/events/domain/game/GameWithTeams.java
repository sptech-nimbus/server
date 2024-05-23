package com.events.events.domain.game;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class GameWithTeams {
    private Object challenger;
    private Object challenged;
    private Game game;

    @Override
    public String toString() {
        return "GameWithTeams {" + '\'' +
                "challenger=" + challenger + '\'' +
                "challenged=" + challenged + '\'' +
                "game=" + game + '\'' +
                "}";
    }
}