package com.events.events.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping
    public ResponseEntity<ResponseMessage<GameResult>> registerGameResult(@RequestBody GameResultDTO dto) {
        return service.register(dto);
    }
}