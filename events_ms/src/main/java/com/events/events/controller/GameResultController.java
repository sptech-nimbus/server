package com.events.events.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.events.events.domain.coach.Coach;
import com.events.events.domain.game.Game;
import com.events.events.domain.gameResult.GameResult;
import com.events.events.domain.gameResult.GameResultDTO;
import com.events.events.domain.responseMessage.ResponseMessage;
import com.events.events.service.GameResultService;

@RestController
@RequestMapping("game-results")
public class GameResultController {
    private final GameResultService service;

    public GameResultController(GameResultService service) {
        this.service = service;
    }

    // POST
    @PostMapping
    public ResponseEntity<ResponseMessage<GameResult>> registerGameResult(@RequestBody GameResultDTO dto) {
        return service.register(dto);
    }

    @GetMapping("not-confirmed-results/{teamId}")
    public ResponseEntity<ResponseMessage<List<Game>>> getNotConfirmedResultsByTeamId(@PathVariable UUID teamId) {
        return service.getNotConfirmedResultsGamesByTeamId(teamId);
    }

    // PATCH
    @PatchMapping("confirm-game-result/{id}")
    public ResponseEntity<ResponseMessage<GameResult>> confirmGameResult(@PathVariable UUID id) {
        return service.confirmGameResult(id);
    }

    @PatchMapping("validate-level/{teamId}")
    public ResponseEntity<?> validateLevel(@PathVariable UUID teamId) {
        service.validateLevel(teamId);

        return ResponseEntity.ok().build();
    }
}