package com.user.user.controller;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.bind.annotation.RestController;

import com.user.user.domain.operationCodes.OperationCode;
import com.user.user.domain.responseMessage.ResponseMessage;
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
            @RequestParam Long now) {
        LocalDateTime date = LocalDateTime.ofInstant(Instant.ofEpochMilli(now), ZoneId.of("UTC"));

        OperationCode opCode = service.getCode(code, date);

        return ResponseEntity.ok(new ResponseMessage<OperationCode>(opCode));
    }
}