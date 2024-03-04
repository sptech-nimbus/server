package com.user.user.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.user.user.models.athleteDesc.AthleteDescDTO;
import com.user.user.models.responseMessage.ResponseMessage;
import com.user.user.services.AthleteDescService;

@SuppressWarnings("rawtypes")
@RestController
@RequestMapping("athleteDesc")
public class AthleteDescController {
    @Autowired
    AthleteDescService service;

    @PostMapping
    public ResponseEntity<ResponseMessage> registerAthleteDesc(@RequestBody AthleteDescDTO dto) {
        return service.register(dto);
    }
}