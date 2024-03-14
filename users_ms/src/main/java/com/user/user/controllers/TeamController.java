package com.user.user.controllers;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.user.user.domains.athlete.Athlete;
import com.user.user.domains.responseMessage.ResponseMessage;
import com.user.user.domains.team.Team;
import com.user.user.domains.team.TeamDTO;
import com.user.user.exceptions.ResourceNotFoundException;
import com.user.user.services.TeamService;

@SuppressWarnings("rawtypes")
@RestController
@RequestMapping("teams")
public class TeamController {
    @Autowired
    TeamService service;

    @PostMapping
    public ResponseEntity<ResponseMessage> registerTeam(@RequestBody TeamDTO dto) {
        try {
            return service.register(dto);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ResponseMessage<>(e.getMessage()));
        }
    }

    @PatchMapping("register-athlete/{id}")
    public ResponseEntity<ResponseMessage> registerAthleteOnTeam(@PathVariable UUID id,
            @RequestBody Athlete athlete) {
        try {
            return service.registerAthleteToTeam(id, athlete);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ResponseMessage<>(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseMessage> putTeamById(@PathVariable UUID id, @RequestBody TeamDTO team) {
        try {
            return service.putTeamById(id, team);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ResponseMessage<>(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseMessage<>(e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<ResponseMessage> getAllTeams() {
        return service.getAllTeams();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseMessage> getTeamById(@PathVariable UUID id) {
        return service.getTeamById(id);
    }

    @GetMapping("/{id}/{nowDate}")
    public ResponseEntity<ResponseMessage> getActiveInjuriesOnTeam(@PathVariable UUID id,
            @PathVariable LocalDate nowDate) {
        return service.getActiveInjuriesOnTeam(id, nowDate);
    }

    @GetMapping("ms-get-team/{id}")
    public ResponseEntity<Team> getTeamByIdByMs(@PathVariable UUID id) {
        return service.getTeamByIdForMs(id);
    }

    @GetMapping("get-team-athletes-asc-age/{id}")
    public ResponseEntity<ResponseMessage> getAthletesByAgeAsc(@PathVariable UUID id) {
        return service.getAthletesByAgeAsc(id);
    }
}
