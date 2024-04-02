package com.events.events.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.events.events.domain.coach.Coach;
import com.events.events.domain.gameResult.GameResult;
import com.events.events.domain.gameResult.GameResultDTO;
import com.events.events.domain.graphs.WinsFromTeamDTO;
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

    // GET
    @GetMapping("/graph/wins-by-team/{teamId}")
    public ResponseEntity<ResponseMessage<WinsFromTeamDTO>> getWinsByTeam(@PathVariable UUID teamId,
            @RequestParam Integer matches) {
        return service.getWinsByTeam(teamId, matches);
    }

    // PATCH
    @PatchMapping("confirm-game-result/{id}")
    public ResponseEntity<ResponseMessage<GameResult>> confirmGameResult(@PathVariable UUID id,
            @RequestBody Coach coach) {
        return service.confirmGameResult(id, coach);
    }
}