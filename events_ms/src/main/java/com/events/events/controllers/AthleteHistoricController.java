package com.events.events.controllers;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.events.events.domains.athleteHistoric.AthleteHistoricDTO;
import com.events.events.domains.responseMessage.ResponseMessage;
import com.events.events.services.AthleteHistoricService;

@SuppressWarnings("rawtypes")
@RestController
@RequestMapping("athlete-historics")
public class AthleteHistoricController {
    private final AthleteHistoricService service;

    public AthleteHistoricController(AthleteHistoricService service) {
        this.service = service;
    }

    // POST
    @PostMapping
    public ResponseEntity<ResponseMessage> postAthleteHistoric(@RequestBody AthleteHistoricDTO dto) {
        return service.register(dto);
    }

    // GET
    @GetMapping("/from-athlete/{athleteId}")
    public ResponseEntity<ResponseMessage> getAthleteHistoricByAthleteId(@PathVariable UUID athleteId) {
        return service.getAthleteHistoricsByAthleteId(athleteId);
    }
}