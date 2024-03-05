package com.user.user.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.user.user.domains.injury.InjuryDTO;
import com.user.user.domains.responseMessage.ResponseMessage;
import com.user.user.services.InjuryService;

@SuppressWarnings("rawtypes")
@RestController
@RequestMapping("injuries")
public class InjuryController {
    @Autowired
    InjuryService service;

    @PostMapping
    public ResponseEntity<ResponseMessage> registerInjury(@RequestBody InjuryDTO dto) {
        return service.register(dto);
    }
}