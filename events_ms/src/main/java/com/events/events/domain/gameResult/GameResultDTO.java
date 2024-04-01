package com.events.events.domain.gameResult;

import com.events.events.domain.game.Game;

public record GameResultDTO(Game game, Integer challengerPoints, Integer challengedPoints) {

}
