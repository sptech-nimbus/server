package com.user.user.controllers;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.user.user.domains.responseMessage.ResponseMessage;
import com.user.user.exceptions.ResourceNotFoundException;
import com.user.user.services.OperationCodeService;

@RestController
@RequestMapping("codes")
@SuppressWarnings("rawtypes")
public class OperationCodeController {
    private final OperationCodeService service;

    public OperationCodeController(OperationCodeService service) {
        this.service = service;
    }

    @GetMapping("validate-code/{code}/{date}")
    public ResponseEntity<ResponseMessage> validateCode(@PathVariable String code, @PathVariable LocalDateTime date) {
        try {
            return service.getCode(code, date);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(401).body(new ResponseMessage("Código não encontrado"));
        }
    }
}
