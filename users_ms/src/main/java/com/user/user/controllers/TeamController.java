package com.user.user.controllers;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.user.user.domains.responseMessage.ResponseMessage;
import com.user.user.domains.team.RegisterAthleteDTO;
import com.user.user.domains.team.TeamDTO;
import com.user.user.services.TeamService;

@SuppressWarnings("rawtypes")
@RestController
@RequestMapping("teams")
public class TeamController {
    @Autowired
    TeamService service;

    @PostMapping
    public ResponseEntity<ResponseMessage> registerTeam(@RequestBody TeamDTO dto) {
        return service.register(dto);
    }

    @PutMapping("/register-athlete")
    public ResponseEntity<ResponseMessage> registerAthleteOnTeam(@RequestBody RegisterAthleteDTO dto) {
        return service.registerAthleteToTeam(dto);
    }

    @GetMapping
    public ResponseEntity<ResponseMessage> getAllTeams() {
        return service.getAllTeams();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseMessage> getTeamById(@PathVariable String id) {
        return service.getTeamById(id);
    }

    @GetMapping("/{id}/{nowDate}")
    public ResponseEntity<ResponseMessage> getActiveInjuriesOnTeam(@PathVariable String id, @PathVariable LocalDate nowDate) {
        return service.getActiveInjuriesOnTeam(id, nowDate);
    }
}
