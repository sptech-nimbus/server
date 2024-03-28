package com.user.user.controller;

import java.time.LocalDate;
import java.util.UUID;

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

import com.user.user.domain.responseMessage.ResponseMessage;
import com.user.user.domain.team.ChangeTeamOwnerAcceptDTO;
import com.user.user.domain.team.ChangeTeamOwnerRequestDTO;
import com.user.user.domain.team.Team;
import com.user.user.domain.team.TeamDTO;
import com.user.user.exception.ResourceNotFoundException;
import com.user.user.service.TeamService;

@SuppressWarnings("rawtypes")
@RestController
@RequestMapping("teams")
public class TeamController {
    private final TeamService service;

    public TeamController(TeamService service) {
        this.service = service;
    }

    // POST
    @PostMapping
    public ResponseEntity<ResponseMessage> registerTeam(@RequestBody TeamDTO dto) {
        try {
            return service.register(dto);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ResponseMessage(e.getMessage()));
        }
    }

    // GET
    @GetMapping
    public ResponseEntity<ResponseMessage> getAllTeams() {
        return service.getAllTeams();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseMessage> getTeamById(@PathVariable UUID id) {
        return service.getTeamById(id);
    }

    @GetMapping("/active-injuries/{id}")
    public ResponseEntity<ResponseMessage> getActiveInjuriesOnTeam(@PathVariable UUID id,
            @RequestParam LocalDate nowDate) {
        try {
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
    public ResponseEntity<ResponseMessage> getAthletesByAgeAsc(@PathVariable UUID id) {
        return service.getAthletesByAgeAsc(id);
    }

    // PUT
    @PutMapping("/{id}")
    public ResponseEntity<ResponseMessage> putTeamById(@PathVariable UUID id, @RequestBody TeamDTO team) {
        try {
            return service.putTeamById(id, team);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ResponseMessage(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseMessage(e.getMessage()));
        }
    }

    // PATCH
    @PatchMapping("change-owner-request/{id}")
    public ResponseEntity<ResponseMessage> changeTeamOwnerRequest(@PathVariable UUID id,
            @RequestBody ChangeTeamOwnerRequestDTO dto) {
        return service.changeTeamOwnerRequest(id, dto);
    }

    @PatchMapping("change-team-owner-by-code/{id}")
    public ResponseEntity<ResponseMessage> changeTeamOwnerAccept(@PathVariable UUID id, @RequestBody ChangeTeamOwnerAcceptDTO dto) {
        try {
            return service.changeTeamOwnerAccept(id, dto);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ResponseMessage("Código inválido"));
        }
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseMessage> deleteTeamById(@PathVariable UUID id, @RequestBody String coachPassword) {
        try {
            return service.deleteTeam(id, coachPassword);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ResponseMessage(e.getMessage()));
        }
    }
}
