package com.user.user.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.user.user.domains.athleteDesc.AthleteDescDTO;
import com.user.user.domains.responseMessage.ResponseMessage;
import com.user.user.services.AthleteDescService;

@SuppressWarnings("rawtypes")
@RestController
@RequestMapping("athleteDescs")
public class AthleteDescController {
    @Autowired
    AthleteDescService service;

    @PostMapping
    public ResponseEntity<ResponseMessage> registerAthleteDesc(@RequestBody AthleteDescDTO dto) {
        return service.register(dto);
    }

    @GetMapping("/{athleteId}")
    public ResponseEntity<ResponseMessage> getAthleteDescsByAthleteId(@PathVariable String athleteId) {
        return service.getAthleteDescsByAthleteId(athleteId);
    }
}