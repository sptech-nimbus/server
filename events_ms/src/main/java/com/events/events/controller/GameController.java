package com.events.events.controller;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.events.events.domain.coach.Coach;
import com.events.events.domain.game.Game;
import com.events.events.domain.game.GameDTO;
import com.events.events.domain.game.GameWithTeams;
import com.events.events.domain.game.GamewResultsDTO;
import com.events.events.domain.responseMessage.ResponseMessage;
import com.events.events.exception.ResourceNotFoundException;
import com.events.events.service.GameService;

@RestController
@RequestMapping("games")
public class GameController {
    private final GameService service;

    public GameController(GameService service) {
        this.service = service;
    }

    // POST
    @PostMapping
    public ResponseEntity<ResponseMessage<List<Game>>> registerGames(@RequestBody List<GameDTO> dtos) {
        return service.register(dtos);
    }

    // GET
    @GetMapping("{teamId}")
    public ResponseEntity<ResponseMessage<List<Game>>> getGamesFromTeamId(@PathVariable UUID teamId) {
        List<Game> games = service.getGamesFromTeamId(teamId);

        if (games.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(new ResponseMessage<>(games));
    }

    @GetMapping("ms-get-by-id/{id}")
    public Game msGetGameById(@PathVariable UUID id) {
        return service.msGetGameById(id);
    }

    @GetMapping("last-game/{teamId}")
    public ResponseEntity<ResponseMessage<GamewResultsDTO>> getLastGame(@PathVariable UUID teamId,
            @RequestParam Long now) {
        try {
            LocalDateTime date = LocalDateTime.ofInstant(Instant.ofEpochMilli(now), ZoneId.of("UTC"));

            GamewResultsDTO gameFound = service.getLastGame(teamId, date);

            return ResponseEntity.ok(new ResponseMessage<GamewResultsDTO>(gameFound));
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("next-game/{teamId}")
    public ResponseEntity<ResponseMessage<GameWithTeams>> getNextGame(@PathVariable UUID teamId, @RequestParam Long now) {
        LocalDateTime date = LocalDateTime.ofInstant(Instant.ofEpochMilli(now), ZoneId.of("UTC"));

        GameWithTeams nextGame = service.getNextGameFromTeamId(teamId, date);

        return ResponseEntity.ok(new ResponseMessage<GameWithTeams>(nextGame));
    }

    // PATCH
    @PatchMapping("confirm-game/{id}")
    public ResponseEntity<ResponseMessage<Game>> confirmGame(@PathVariable UUID id, @RequestBody Coach coach) {
        try {
            return service.confirmGame(id, coach);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ResponseMessage<Game>(e.getMessage()));
        }
    }

    // DELETE
    @DeleteMapping("{id}")
    public ResponseEntity<ResponseMessage<Game>> cancelGameById(@PathVariable UUID id, @RequestBody Coach coach) {
        try {
            return service.cancelGameById(id, coach);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ResponseMessage<Game>(e.getMessage()));
        }
    }
}