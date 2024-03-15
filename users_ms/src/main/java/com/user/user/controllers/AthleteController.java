package com.user.user.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.user.user.domains.athlete.Athlete;
import com.user.user.domains.athlete.AthleteDTO;
import com.user.user.domains.responseMessage.ResponseMessage;
import com.user.user.exceptions.ResourceNotFoundException;
import com.user.user.services.AthleteService;

@SuppressWarnings("rawtypes")
@RestController
@RequestMapping("athletes")
public class AthleteController {
    @Autowired
    AthleteService service;

    @PutMapping("/{id}")
    public ResponseEntity<ResponseMessage> putAthlete(@PathVariable UUID id, @RequestBody AthleteDTO dto) {
        return service.putPersona(id, dto);
    }

    @GetMapping("ms-get-athlete/{id}")
    public ResponseEntity<Athlete> getAthleteForMs(@PathVariable UUID id) {
        try{
            return service.getAthleteForMs(id);
        }catch(ResourceNotFoundException e) {
            return ResponseEntity.status(404).build();
        }
    }
}