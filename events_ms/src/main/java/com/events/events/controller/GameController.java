package com.events.events.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.events.events.domain.coach.Coach;
import com.events.events.domain.game.GameDTO;
import com.events.events.domain.responseMessage.ResponseMessage;
import com.events.events.exception.ResourceNotFoundException;
import com.events.events.service.GameService;

@SuppressWarnings("rawtypes")
@RestController
@RequestMapping("games")
public class GameController {
    private final GameService service;

    public GameController(GameService service) {
        this.service = service;
    }

    // POST
    @PostMapping
    public ResponseEntity<ResponseMessage> registerGame(@RequestBody GameDTO dto) {
        return service.register(dto);
    }

    // GET
    @GetMapping("{teamId}")
    public ResponseEntity<ResponseMessage> getGamesFromTeamId(@PathVariable UUID teamId) {
        return service.getGamesFromTeamId(teamId);
    }

    // PATCH
    @PatchMapping("confirm-game/{id}")
    public ResponseEntity<ResponseMessage> confirmGame(@PathVariable UUID id, @RequestBody Coach coach) {
        try {
            return service.confirmGame(id, coach);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ResponseMessage(e.getMessage()));
        }
    }

    // DELETE
    @DeleteMapping("{id}")
    public ResponseEntity<ResponseMessage> cancelGameById(@PathVariable UUID id, @RequestBody Coach coach) {
        try {
            return service.cancelGameById(id, coach);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ResponseMessage(e.getMessage()));
        }
    }
}