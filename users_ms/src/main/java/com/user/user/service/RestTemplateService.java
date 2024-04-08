package com.user.user.service;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RestTemplateService<T> {
    private final RestTemplate restTemplate;

    public RestTemplateService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public T getTemplateById(String port, String endPoint, UUID id, Class<T> classType) throws Exception {
        try {
            ResponseEntity<T> restResponseEntity = restTemplate.getForEntity(
                    "http://localhost:" + port + "/" + endPoint + "/" + id, classType);

            return restResponseEntity.getBody();
        } catch (Exception e) {
            throw e;
        }
    }
}