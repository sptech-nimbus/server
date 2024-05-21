package com.events.events.domain.game;

import com.events.events.domain.gameResult.GameResult;

public record GamewResultsDTO(Game game, GameResult result, Object challenger, Object challenged) {
}
