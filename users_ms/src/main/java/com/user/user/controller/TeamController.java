package com.user.user.controller;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.azure.core.annotation.Get;
import com.user.user.domain.athlete.Athlete;
import com.user.user.domain.athlete.InjuredAthleteDTO;
import com.user.user.domain.athleteHistoric.ForecastDTO;
import com.user.user.domain.responseMessage.ResponseMessage;
import com.user.user.domain.team.ChangeTeamOwnerAcceptDTO;
import com.user.user.domain.team.ChangeTeamOwnerRequestDTO;
import com.user.user.domain.team.Team;
import com.user.user.domain.team.TeamDTO;
import com.user.user.exception.ResourceNotFoundException;
import com.user.user.service.TeamService;


@RestController
@RequestMapping("teams")
public class TeamController {
    private final TeamService service;

    public TeamController(TeamService service) {
        this.service = service;
    }

    // POST
    @PostMapping
    public ResponseEntity<ResponseMessage<Team>> registerTeam(@RequestBody TeamDTO dto) {
        return service.register(dto);
    }

    @GetMapping("generate-csv/{teamId}")
    public ResponseEntity<?> recordingCsv(@PathVariable UUID teamId) {
        try {
            return service.generateCSV(teamId);
        } catch (IOException e) {
            return ResponseEntity.status(500).build();
        }
    }

    // GET
    @GetMapping
    public ResponseEntity<ResponseMessage<List<Team>>> getAllTeams() {
        return service.getAllTeams();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseMessage<Team>> getTeamById(@PathVariable UUID id) {
        return service.getTeamById(id);
    }

    @GetMapping("by-coach/{coachId}")
    public ResponseEntity<ResponseMessage<List<Team>>> getTeamsByCoachId(@PathVariable UUID coachId) {
        List<Team> teamsFound = service.findByCoachId(coachId);

        if (teamsFound.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(new ResponseMessage<>(teamsFound));
    }

    @GetMapping("by-athlete/{athleteId}")
    public ResponseEntity<ResponseMessage<Team>> getTeamByAthlete(@PathVariable UUID athleteId) {
        Team teamFound = service.getTeamByAthlete(athleteId);

        return ResponseEntity.ok(new ResponseMessage<>(teamFound));
    }

    @GetMapping("/active-injuries/{id}")
    public ResponseEntity<ResponseMessage<List<InjuredAthleteDTO>>> getActiveInjuriesOnTeam(@PathVariable UUID id,
            @RequestParam Long now) {
        try {
            LocalDate nowDate = LocalDate.ofInstant(Instant.ofEpochMilli(now), ZoneId.of("UTC"));

            return service.getActiveInjuriesOnTeam(id, nowDate);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ResponseMessage<>(e.getMessage()));
        }
    }

    @GetMapping("ms-get-team/{id}")
    public ResponseEntity<Team> getTeamByIdByMs(@PathVariable UUID id) {
        try {
            return service.getTeamByIdForMs(id);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).build();
        }
    }

    @GetMapping("get-team-athletes-asc-age/{id}")
    public ResponseEntity<ResponseMessage<List<Athlete>>> getAthletesByAgeAsc(@PathVariable UUID id) {
        return service.getAthletesByAgeAsc(id);
    }

    @GetMapping("generate-forecast/{challengerId}/{challengedId}")
    public ResponseEntity<?> generateForecast(@PathVariable UUID challengerId, @PathVariable UUID challengedId) {
        try {
            // Chama o serviço para gerar a previsão
            ForecastDTO forecast = service.generateForecast(challengerId, challengedId);
            
            // Retorna uma resposta com sucesso
            return ResponseEntity.ok(forecast);
        } catch (Exception e) {
            // Retorna uma resposta de erro
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(new ResponseMessage<>("Error generating forecast: " + e.getMessage()));
        }
    }

    // PUT
    @PutMapping("/{id}")
    public ResponseEntity<ResponseMessage<?>> putTeamById(@PathVariable UUID id, @RequestBody TeamDTO team) {
        try {
            return service.putTeamById(id, team);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ResponseMessage<>(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseMessage<>(e.getMessage()));
        }
    }

    // PATCH
    @PatchMapping("change-owner-request/{id}")
    public ResponseEntity<ResponseMessage<?>> changeTeamOwnerRequest(@PathVariable UUID id,
            @RequestBody ChangeTeamOwnerRequestDTO dto) {
        return service.changeTeamOwnerRequest(id, dto);
    }

    @PatchMapping("change-team-owner-by-code/{id}")
    public ResponseEntity<ResponseMessage<?>> changeTeamOwnerAccept(@PathVariable UUID id,
            @RequestBody ChangeTeamOwnerAcceptDTO dto) {
        try {
            return service.changeTeamOwnerAccept(id, dto);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ResponseMessage<>("Código inválido"));
        }
    }

    // @PostMapping("ms-change-level/{id}")
    // public ResponseEntity<ResponseMessage<?>> changeLevel(@PathVariable UUID id, @RequestParam Integer level) {
    //     service.alterarLevel(id, level);

    //     return ResponseEntity.status(200).build();
    // }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseMessage<?>> deleteTeamById(@PathVariable UUID id, @RequestBody String coachPassword) {
        try {
            return service.deleteTeam(id, coachPassword);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ResponseMessage<>(e.getMessage()));
        }
    }
}
