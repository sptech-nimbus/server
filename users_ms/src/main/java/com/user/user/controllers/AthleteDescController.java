package com.user.user.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.user.user.domains.athleteDesc.AthleteDescDTO;
import com.user.user.domains.responseMessage.ResponseMessage;
import com.user.user.exceptions.ResourceNotFoundException;
import com.user.user.services.AthleteDescService;

@SuppressWarnings("rawtypes")
@RestController
@RequestMapping("athlete-descs")
public class AthleteDescController {
    private final AthleteDescService service;

    @Autowired
    public AthleteDescController(AthleteDescService service) {
        this.service = service;
    }

    // POST
    @PostMapping
    public ResponseEntity<ResponseMessage> registerAthleteDesc(@RequestBody AthleteDescDTO dto) {
        try {
            return service.register(dto);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ResponseMessage(e.getMessage()));
        }
    }

    // GET
    @GetMapping("/{athleteId}")
    public ResponseEntity<ResponseMessage> getAthleteDescsByAthleteId(@PathVariable UUID athleteId) {
        try {
            return service.getAthleteDescsByAthleteId(athleteId);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ResponseMessage(e.getMessage()));
        }
    }

    // PUT
    @PutMapping("/{athleteId}")
    public ResponseEntity<ResponseMessage> putAthleteDescByAthleteId(@PathVariable UUID athleteId,
            @RequestBody AthleteDescDTO dto) {
        try {
            return service.putAthleteDescByAthleteId(athleteId, dto);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ResponseMessage(e.getMessage()));
        }
    }
}