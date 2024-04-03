package com.events.events.domain.graphs;

import com.events.events.domain.game.Game;

public record ThreePointsConvertedDTO(Integer threePointsAttemped, Integer threePointsConverted, Game game) {
}