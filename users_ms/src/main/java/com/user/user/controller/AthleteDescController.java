package com.user.user.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.user.user.domain.athleteDesc.AthleteDesc;
import com.user.user.domain.athleteDesc.AthleteDescDTO;
import com.user.user.domain.athleteDesc.AthleteDescwAthleteDTO;
import com.user.user.domain.responseMessage.ResponseMessage;
import com.user.user.exception.ResourceNotFoundException;
import com.user.user.service.AthleteDescService;

@RestController
@RequestMapping("athlete-descs")
public class AthleteDescController {
    private final AthleteDescService service;

    public AthleteDescController(AthleteDescService service) {
        this.service = service;
    }

    // POST
    @PostMapping
    public ResponseEntity<ResponseMessage<AthleteDesc>> registerAthleteDesc(@RequestBody AthleteDescDTO dto) {
        try {
            return service.register(dto);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ResponseMessage<>(e.getMessage()));
        }
    }

    // GET
    @GetMapping("/{athleteId}")
    public ResponseEntity<ResponseMessage<AthleteDesc>> getAthleteDescsByAthleteId(@PathVariable UUID athleteId) {
        try {
            return service.getAthleteDescsByAthleteId(athleteId);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ResponseMessage<>(e.getMessage()));
        }
    }

    @GetMapping("/all-info/{athleteId}")
    public ResponseEntity<ResponseMessage<AthleteDescwAthleteDTO>> getAthleteAllInfo(@PathVariable UUID athleteId) {
        AthleteDescwAthleteDTO athleteInfo = service.getAthleteAllInfo(athleteId);

        return ResponseEntity.ok(new ResponseMessage<AthleteDescwAthleteDTO>(athleteInfo));
    }

    // PUT
    @PutMapping("/{athleteId}")
    public ResponseEntity<ResponseMessage<?>> putAthleteDescByAthleteId(@PathVariable UUID athleteId,
            @RequestBody AthleteDescDTO dto) {
        try {
            return service.putAthleteDescByAthleteId(athleteId, dto);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ResponseMessage<>(e.getMessage()));
        }
    }
}