package com.events.events.controllers;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.events.events.domains.coach.Coach;
import com.events.events.domains.game.GameDTO;
import com.events.events.domains.responseMessage.ResponseMessage;
import com.events.events.exceptions.ResourceNotFoundException;
import com.events.events.services.GameService;

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
    @SendTo("/events/{teamId}")
    @MessageMapping("/register-game/{teamId}")
    public ResponseEntity<ResponseMessage> registerGame(@RequestBody GameDTO dto, @DestinationVariable UUID teamId) {
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
        return service.confirmGame(id, coach);
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