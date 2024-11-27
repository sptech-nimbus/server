package com.user.user.controller;

import java.io.IOException;
import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.user.user.domain.responseMessage.ResponseMessage;
import com.user.user.service.AzureBlobService;

@RestController
@RequestMapping(path = "blob-storage", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE,
        MediaType.APPLICATION_JSON_VALUE })
public class BlobStorageController {
    private final AzureBlobService service;

    public BlobStorageController(AzureBlobService service) {
        this.service = service;
    }

    @PostMapping("{entityId}")
    public ResponseEntity<ResponseMessage<?>> registerStorage(@PathVariable UUID entityId,
            @RequestParam MultipartFile file) {
        try {
            service.uploadImage(entityId, file);

            return ResponseEntity.status(200).body(new ResponseMessage<>("Solicitação de registro enviada"));
        } catch (IOException e) {
            return ResponseEntity.status(400).body(new ResponseMessage<>(e.getMessage()));
        }
    }
}