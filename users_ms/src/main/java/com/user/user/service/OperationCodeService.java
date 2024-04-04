package com.user.user.service;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.user.user.domain.operationCodes.OperationCode;
import com.user.user.domain.responseMessage.ResponseMessage;
import com.user.user.exception.ResourceNotFoundException;
import com.user.user.repository.OperationCodeRepository;

@Service
public class OperationCodeService {
    private final OperationCodeRepository repo;

    public OperationCodeService(OperationCodeRepository repo) {
        this.repo = repo;
    }

    public ResponseEntity<ResponseMessage<?>> getCode(String code, LocalDateTime date) {
        try {
            if (!validateCode(code, date)) {
                return ResponseEntity.status(401).body(new ResponseMessage<>("Código expirado"));
            }
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(code, null);
        }

        return ResponseEntity.status(200).build();
    }

    public ResponseEntity<ResponseMessage<?>> insertCode(OperationCode operationCode) {
        try {
            repo.save(operationCode);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseMessage<>("Erro ao cadastrar código", e.getMessage()));
        }

        return ResponseEntity.status(200).build();
    }

    public Boolean validateCode(String code, LocalDateTime date) {
        OperationCode codeFound = repo.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("código", null));

        if (codeFound.getExpirationDate().isBefore(date))
            return false;

        return true;
    }
}
