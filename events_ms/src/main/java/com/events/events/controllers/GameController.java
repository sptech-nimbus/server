package com.events.events.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.events.events.domains.game.GameDTO;
import com.events.events.domains.responseMessage.ResponseMessage;
import com.events.events.services.GameService;

@SuppressWarnings("rawtypes")
@RestController
@RequestMapping("games")
public class GameController {
    @Autowired
    GameService service;

    @PostMapping
    public ResponseEntity<ResponseMessage> registerGame(@RequestBody GameDTO dto) {
        return service.register(dto);
    }

    @GetMapping("/{teamId}")
    public ResponseEntity<ResponseMessage> getGamesFromTeamId(@PathVariable UUID teamId) {
        return service.getGamesFromTeamId(teamId);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ResponseMessage> cancelGameById(@PathVariable UUID id) {
        return service.cancelGameById(id);
    }
}