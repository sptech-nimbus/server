package com.user.user.controllers;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.user.user.domains.athlete.Athlete;
import com.user.user.domains.athlete.AthleteDTO;
import com.user.user.domains.responseMessage.ResponseMessage;
import com.user.user.domains.team.Team;
import com.user.user.exceptions.ResourceNotFoundException;
import com.user.user.services.AthleteService;

@SuppressWarnings("rawtypes")
@RestController
@RequestMapping("athletes")
public class AthleteController {
    private final AthleteService service;

    public AthleteController(AthleteService service) {
        this.service = service;
    }

    // GET
    @GetMapping("ms-get-athlete/{id}")
    public ResponseEntity<Athlete> getAthleteForMs(@PathVariable UUID id) {
        try {
            return service.getAthleteForMs(id);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).build();
        }
    }

    // PUT
    @PutMapping("/{id}")
    public ResponseEntity<ResponseMessage> putAthlete(@PathVariable UUID id, @RequestBody AthleteDTO dto) {
        try {
            return service.putPersona(id, dto);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ResponseMessage(e.getMessage()));
        }
    }

    // PATCH
    @PatchMapping("register-team/{id}")
    public ResponseEntity<ResponseMessage> registerAthleteOnTeam(@PathVariable UUID id,
            @RequestBody Team team) {
        try {
            return service.registerAthleteToTeam(id, team);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ResponseMessage(e.getMessage()));
        }
    }
}