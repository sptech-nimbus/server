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

import com.user.user.domains.coach.CoachDTO;
import com.user.user.domains.coach.sCoachDTO;
import com.user.user.domains.responseMessage.ResponseMessage;
import com.user.user.services.CoachService;

@SuppressWarnings("rawtypes")
@RestController
@RequestMapping("coaches")
public class CoachController {
    @Autowired
    CoachService service;

    @GetMapping("/ms-get-coach/{id}")
    public ResponseEntity<sCoachDTO> getCoachById(@PathVariable UUID id) {
        return service.getCoachById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseMessage> putAthlete(@PathVariable UUID id, @RequestBody CoachDTO dto) {
        return service.putPersona(id, dto);
    }
}
