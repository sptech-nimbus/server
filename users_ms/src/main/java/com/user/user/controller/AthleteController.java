package com.user.user.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.user.user.domain.athlete.Athlete;
import com.user.user.domain.athlete.AthleteDTO;
import com.user.user.domain.responseMessage.ResponseMessage;
import com.user.user.domain.team.Team;
import com.user.user.exception.ResourceNotFoundException;
import com.user.user.service.AthleteService;

@RestController
@RequestMapping("athletes")
public class AthleteController {
    private final AthleteService service;

    public AthleteController(AthleteService service) {
        this.service = service;
    }

    // GET
    @GetMapping("ms-get-athlete/{id}")
    public Athlete getAthleteForMs(@PathVariable UUID id) throws ResourceNotFoundException {
        try {
            return service.getAthleteForMs(id).getBody();
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("atleta", id);
        }
    }

    @GetMapping("by-team/{teamId}")
    public ResponseEntity<ResponseMessage<List<Athlete>>> findByTeam(@PathVariable UUID teamId) {
        List<Athlete> athletesFound = service.findByTeam(teamId);

        if (athletesFound.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(new ResponseMessage<List<Athlete>>(athletesFound));
    }

    // PUT
    @PutMapping("/{id}")
    public ResponseEntity<ResponseMessage<?>> putAthlete(@PathVariable UUID id, @RequestBody AthleteDTO dto) {
        try {
            return service.putPersona(id, dto);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ResponseMessage<>(e.getMessage()));
        }
    }

    // PATCH
    @PatchMapping("register-team/{id}")
    public ResponseEntity<ResponseMessage<?>> registerAthleteOnTeam(@PathVariable UUID id,
            @RequestBody Team team) {
        try {
            return service.registerAthleteToTeam(id, team);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ResponseMessage<>(e.getMessage()));
        }
    }

    @PatchMapping("replace-starting/{id}")
    public ResponseEntity<ResponseMessage<?>> replaceIsStartingById(@PathVariable UUID id) {
        try{
            return service.replaceIsStating(id);
        }catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ResponseMessage<>(e.getMessage()));
        }
    }
    
}