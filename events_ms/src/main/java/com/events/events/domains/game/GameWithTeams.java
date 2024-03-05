package com.events.events.domains.game;

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
}