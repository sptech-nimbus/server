package com.events.events.controllers;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.events.events.domains.responseMessage.ResponseMessage;
import com.events.events.domains.training.TrainingDTO;
import com.events.events.exceptions.ResourceNotFoundException;
import com.events.events.services.TrainingService;

@SuppressWarnings("rawtypes")
@RestController
@RequestMapping("trainings")
public class TrainingController {
    private final TrainingService service;

    public TrainingController(TrainingService service) {
        this.service = service;
    }

    // POST
    @PostMapping
    public ResponseEntity<ResponseMessage> registerTraining(@RequestBody TrainingDTO dto) {
        return service.register(dto);
    }

    // GET
    @GetMapping("{teamId}/{page}/{elements}")
    public ResponseEntity<ResponseMessage> getTrainingsPageByTeamId(@PathVariable UUID teamId,
            @PathVariable Integer page, @PathVariable Integer elements) {
        return service.getTrainingsPageByTeamId(teamId, page, elements);
    }

    // PUT
    @PutMapping("{id}")
    public ResponseEntity<ResponseMessage> putTraining(@PathVariable UUID id, @RequestBody TrainingDTO dto) {
        try {
            return service.putTraining(id, dto);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ResponseMessage(e.getMessage()));
        }
    }

    // DELETE
    @DeleteMapping("{id}")
    public ResponseEntity<ResponseMessage> deleteTraining(@PathVariable UUID id) {
        try {
            return service.deleteTraining(id);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ResponseMessage(e.getMessage()));
        }
    }
}