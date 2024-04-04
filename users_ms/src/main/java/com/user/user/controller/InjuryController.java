package com.user.user.controller;

import java.util.List;
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

import com.user.user.domain.injury.Injury;
import com.user.user.domain.injury.InjuryDTO;
import com.user.user.domain.responseMessage.ResponseMessage;
import com.user.user.exception.ResourceNotFoundException;
import com.user.user.service.InjuryService;

@RestController
@RequestMapping("injuries")
public class InjuryController {
    private final InjuryService service;

    public InjuryController(InjuryService service) {
        this.service = service;
    }

    // POST
    @PostMapping
    public ResponseEntity<ResponseMessage<Injury>> registerInjury(@RequestBody InjuryDTO dto) {
        return service.register(dto);
    }

    // GET
    @GetMapping("from-athlete/{athleteId}")
    public ResponseEntity<ResponseMessage<List<Injury>>> getByAthleteId(@PathVariable UUID athleteId) {
        return service.getByAthleteId(athleteId);
    }

    // PUT
    @PutMapping("{id}")
    public ResponseEntity<ResponseMessage<?>> putInjury(@PathVariable UUID id, @RequestBody InjuryDTO dto) {
        try {
            return service.putInjury(id, dto);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ResponseMessage<>(e.getMessage()));
        }
    }

    // DELETE
    @DeleteMapping("{id}")
    public ResponseEntity<ResponseMessage<?>> deleteInjury(@PathVariable UUID id) {
        try {
            return service.deleteInjury(id);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ResponseMessage<>(e.getMessage()));
        }
    }
}