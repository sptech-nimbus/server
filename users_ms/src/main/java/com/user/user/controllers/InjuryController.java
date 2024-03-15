package com.user.user.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.user.user.domains.injury.InjuryDTO;
import com.user.user.domains.responseMessage.ResponseMessage;
import com.user.user.exceptions.ResourceNotFoundException;
import com.user.user.services.InjuryService;

@SuppressWarnings("rawtypes")
@RestController
@RequestMapping("injuries")
public class InjuryController {
    @Autowired
    InjuryService service;

    // POST
    @PostMapping
    public ResponseEntity<ResponseMessage> registerInjury(@RequestBody InjuryDTO dto) {
        return service.register(dto);
    }

    // PUT
    @PutMapping("{id}")
    public ResponseEntity<ResponseMessage> putInjury(@PathVariable UUID id, @RequestBody InjuryDTO dto) {
        try {
            return service.putInjury(id, dto);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ResponseMessage(e.getMessage()));
        }
    }

    // DELETE
    @DeleteMapping("{id}")
    public ResponseEntity<ResponseMessage> deleteInjury(@PathVariable UUID id) {
        try {
            return service.deleteInjury(id);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ResponseMessage(e.getMessage()));
        }
    }
}