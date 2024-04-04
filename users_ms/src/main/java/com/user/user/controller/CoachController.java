package com.user.user.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.user.user.domain.coach.CoachDTO;
import com.user.user.domain.coach.sCoachDTO;
import com.user.user.domain.responseMessage.ResponseMessage;
import com.user.user.exception.ResourceNotFoundException;
import com.user.user.service.CoachService;

@RestController
@RequestMapping("coaches")
public class CoachController {
    private final CoachService service;

    public CoachController(CoachService service) {
        this.service = service;
    }

    // GET
    @GetMapping("/ms-get-coach/{id}")
    public ResponseEntity<sCoachDTO> getCoachById(@PathVariable UUID id) {
        try {
            return service.getCoachById(id);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).build();
        }
    }

    // PUT
    @PutMapping("/{id}")
    public ResponseEntity<ResponseMessage<?>> putCoach(@PathVariable UUID id, @RequestBody CoachDTO dto) {
        try {
            return service.putPersona(id, dto);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ResponseMessage<>(e.getMessage()));
        }
    }
}
