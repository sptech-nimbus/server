package com.events.events.controller;

import java.util.List;
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
import com.events.events.domain.graphs.ThreePointsConvertedDTO;
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

    @GetMapping("three-points-by-team-matches/{teamId}")
    public ResponseEntity<ResponseMessage<List<ThreePointsConvertedDTO>>> getThreePointsConverteAndAttempeddByTeam(
            @PathVariable UUID teamId, @RequestParam Integer matches) {
        return service.getThreePointsConverteAndAttempeddByTeam(teamId, matches);
    }

    @GetMapping("points-division-by-team-matches/{teamId}")
    public ResponseEntity<ResponseMessage<PointsDivisionDTO>> getPointsDivisionByTeamMatches(@PathVariable UUID teamId,
            @RequestParam Integer matches) {
        return service.getPointsDivisionByTeamMatches(teamId, matches);
    }

    @GetMapping("points-per-game/{teamId}")
    public ResponseEntity<ResponseMessage<Map<Game, Integer>>> getPointsPerGameFromTeamId(@PathVariable UUID teamId,
            @RequestParam Integer matches) {
        Map<Game, Integer> pointsPerGame = service.getPointsPerGame(teamId, matches);

        if (pointsPerGame.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(new ResponseMessage<Map<Game, Integer>>(pointsPerGame));
    }
}
