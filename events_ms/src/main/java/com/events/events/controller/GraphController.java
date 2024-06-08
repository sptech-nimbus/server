package com.events.events.controller;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.events.events.domain.game.Game;
import com.events.events.domain.graphs.PointsDivisionDTO;
import com.events.events.domain.graphs.ReboundsPerTeam;
import com.events.events.domain.graphs.WinsFromTeamDTO;
import com.events.events.domain.responseMessage.ResponseMessage;
import com.events.events.service.GraphService;

@RestController
@RequestMapping("graphs")
public class GraphController {
    private final GraphService service;

    public GraphController(GraphService service) {
        this.service = service;
    }

    // GET
    @GetMapping("wins-by-team-matches/{teamId}")
    public ResponseEntity<ResponseMessage<WinsFromTeamDTO>> getWinsByTeam(@PathVariable UUID teamId,
            @RequestParam Integer matches) {
        return service.getWinsByTeam(teamId, matches);
    }

    @GetMapping("points-division-per-team/{teamId}")
    public ResponseEntity<ResponseMessage<PointsDivisionDTO>> getPointsDivisionByTeamMatches(@PathVariable UUID teamId,
            @RequestParam Integer matches) {
        PointsDivisionDTO pointsDivision = service.getPointsDivisionByTeamMatches(teamId, matches);

        return ResponseEntity.ok(new ResponseMessage<>(pointsDivision));
    }

    @GetMapping("rebounds-per-team/{teamId}")
    public ResponseEntity<ResponseMessage<Map<Game, ReboundsPerTeam>>> getReboundsPerGameFromTeam(
            @PathVariable UUID teamId,
            @RequestParam Integer matches) {
        Map<Game, ReboundsPerTeam> mapReboundsPerGame = service.getReboundsPerGameFromTeam(teamId, matches);

        if (mapReboundsPerGame.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(new ResponseMessage<>(mapReboundsPerGame));
    }

    @GetMapping("points-per-game/{teamId}")
    public ResponseEntity<ResponseMessage<Map<LocalDateTime, Integer>>> getPointsPerGameFromTeamId(@PathVariable UUID teamId,
            @RequestParam Integer matches) {
        Map<LocalDateTime, Integer> pointsPerGame = service.getPointsPerGame(teamId, matches);

        if (pointsPerGame.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(new ResponseMessage<Map<LocalDateTime, Integer>>(pointsPerGame));
    }

    @GetMapping("fouls-per-game/{teamId}")
    public ResponseEntity<ResponseMessage<Map<LocalDateTime, Integer>>> getFoulsPerGameFromTeamId(@PathVariable UUID teamId,
            @RequestParam Integer matches) {
        Map<LocalDateTime, Integer> foulsPerGame = service.getFoulsPerGame(teamId, matches);

        if (foulsPerGame.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(new ResponseMessage<Map<LocalDateTime, Integer>>(foulsPerGame));
    }
}
