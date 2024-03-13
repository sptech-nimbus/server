package com.events.events.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.events.events.domains.responseMessage.ResponseMessage;
import com.events.events.domains.training.TrainingDTO;
import com.events.events.services.TrainingService;

@SuppressWarnings("rawtypes")
@RestController
@RequestMapping("trainings")
public class TrainingController {
    @Autowired
    TrainingService service;

    @PostMapping
    public ResponseEntity<ResponseMessage> registerTraining(@RequestBody TrainingDTO dto) {
        return service.register(dto);
    }
}