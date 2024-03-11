package com.user.user.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.user.user.domains.coach.CoachDTO;
import com.user.user.domains.responseMessage.ResponseMessage;
import com.user.user.services.CoachService;

@SuppressWarnings("rawtypes")
@RestController
@RequestMapping("coaches")
public class CoachController {
    @Autowired
    CoachService service;

    @PutMapping("/{id}")
    public ResponseEntity<ResponseMessage> putAthlete(@PathVariable String id, @RequestBody CoachDTO dto) {
        return service.putPersona(id, dto);
    }
}
