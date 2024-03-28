package com.events.events.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.events.events.domain.athleteHistoric.AthleteHistoricDTO;
import com.events.events.domain.responseMessage.ResponseMessage;
import com.events.events.service.AthleteHistoricService;

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
    @GetMapping("from-athlete/{athleteId}")
    public ResponseEntity<ResponseMessage> getAthleteHistoricByAthleteId(@PathVariable UUID athleteId) {
        return service.getAthleteHistoricsByAthleteId(athleteId);
    }

    @GetMapping("from-athlete/{athleteId}/{page}/{elements}")
    public ResponseEntity<ResponseMessage> getAthleteHistoricPageByAthleteId(@PathVariable UUID athleteId,
            @PathVariable Integer page, @PathVariable Integer elements) {
        return service.getAthleteHistoricsPageByAthleteId(athleteId, page, elements);
    }

    // PUT
    @PutMapping("{id}")
    public ResponseEntity<ResponseMessage> putAthleteHistoric(@PathVariable UUID id,
            @RequestBody AthleteHistoricDTO dto) {
        return service.putAhlteteHistoric(id, dto);
    }
}