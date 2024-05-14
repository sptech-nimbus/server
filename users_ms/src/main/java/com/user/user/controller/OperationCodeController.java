package com.user.user.controller;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.bind.annotation.RestController;

import com.user.user.domain.operationCodes.OperationCode;
import com.user.user.domain.responseMessage.ResponseMessage;
import com.user.user.exception.ResourceNotFoundException;
import com.user.user.service.OperationCodeService;

@RestController
@RequestMapping("codes")
public class OperationCodeController {
    private final OperationCodeService service;

    public OperationCodeController(OperationCodeService service) {
        this.service = service;
    }

    @GetMapping("validate-code")
    public ResponseEntity<ResponseMessage<?>> validateCode(@RequestParam String code,
            @RequestParam LocalDateTime date) {
        try {
            OperationCode codeFound = service.getCode(code, date);
            return ResponseEntity.status(200).body(new ResponseMessage<>(codeFound));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(401).body(new ResponseMessage<>("Código não encontrado"));
        }
    }
}
