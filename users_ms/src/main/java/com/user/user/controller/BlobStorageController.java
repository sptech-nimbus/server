package com.user.user.controller;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.user.user.domain.blob.EntityDTO;
import com.user.user.domain.responseMessage.ResponseMessage;
import com.user.user.exception.ResourceNotFoundException;
import com.user.user.service.AzureBlobService;

@RestController
@RequestMapping(path = "blob-storage", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE,
        MediaType.APPLICATION_JSON_VALUE })
public class BlobStorageController {
    private final AzureBlobService service;

    public BlobStorageController(AzureBlobService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ResponseMessage<?>> registerStorage(@RequestPart EntityDTO entity,
            @RequestParam MultipartFile file) {
        try {
            service.uploadImage(entity.id(), file);

            return ResponseEntity.status(200).body(new ResponseMessage<>("Solicitação de registro enviada"));
        } catch (IOException e) {
            return ResponseEntity.status(500).body(new ResponseMessage<>(e.getMessage()));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ResponseMessage<>(e.getMessage()));
        }
    }
}