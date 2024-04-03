package com.events.events.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.events.events.domain.athleteHistoric.AthleteHistoric;
import com.events.events.domain.athleteHistoric.AthleteHistoricDTO;
import com.events.events.domain.responseMessage.ResponseMessage;
import com.events.events.exception.ResourceNotFoundException;
import com.events.events.service.AthleteHistoricService;

@RestController
@RequestMapping("athlete-historics")
public class AthleteHistoricController {
    private final AthleteHistoricService service;

    public AthleteHistoricController(AthleteHistoricService service) {
        this.service = service;
    }

    // POST
    @PostMapping
    public ResponseEntity<ResponseMessage<AthleteHistoric>> postAthleteHistoric(@RequestBody AthleteHistoricDTO dto) {
        return service.register(dto);
    }

    // GET
    @GetMapping("from-athlete/{athleteId}")
    public ResponseEntity<ResponseMessage<List<AthleteHistoric>>> getAthleteHistoricByAthleteId(
            @PathVariable UUID athleteId) {
        return service.getAthleteHistoricsByAthleteId(athleteId);
    }

    @GetMapping("page-from-athlete/{athleteId}")
    public ResponseEntity<ResponseMessage<Page<AthleteHistoric>>> getAthleteHistoricPageByAthleteId(
            @PathVariable UUID athleteId,
            @RequestParam Integer page, @RequestParam Integer elements) {
        return service.getAthleteHistoricsPageByAthleteId(athleteId, page, elements);
    }

    // PUT
    @PutMapping("{id}")
    public ResponseEntity<ResponseMessage<AthleteHistoric>> putAthleteHistoric(@PathVariable UUID id,
            @RequestBody AthleteHistoricDTO dto) {
        return service.putAhlteteHistoric(id, dto);
    }

    // DELETE
    @DeleteMapping("{id}")
    public ResponseEntity<ResponseMessage<?>> deleteAthleteHistoric(@PathVariable UUID id) {
        try {
            return service.deleteAhleteHistoric(id);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(204).body(new ResponseMessage<>(e.getMessage()));
        }
    }
}