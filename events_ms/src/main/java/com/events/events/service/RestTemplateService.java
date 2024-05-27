package com.events.events.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RestTemplateService<T> {
    @Value("${jwt.secret}")
    private String jwtSecret;

    private final RestTemplate restTemplate;

    public RestTemplateService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public T getTemplateById(String port, String endPoint, UUID id, Class<T> classType) throws Exception {
        try {
            HttpHeaders headers = new HttpHeaders();

            headers.add("jwt-secret", jwtSecret);

            ResponseEntity<T> restResponseEntity = restTemplate.getForEntity(
                    "http://localhost:" + port + "/" + endPoint + "/" + id, classType);

            return restResponseEntity.getBody();
        } catch (Exception e) {
            throw e;
        }
    }

    public T[] getTemplateList(String port, String endPoint, UUID id, String requestParams,
            Class<T[]> classType) {
        try {
            String httpUrl = "http://localhost:" + port + "/" + endPoint + "/" + id + "?" + requestParams;

            ResponseEntity<T[]> restResponseEntity = restTemplate.getForEntity(httpUrl, classType);

            return restResponseEntity.getBody();
        } catch (Exception e) {
            throw e;
        }
    }

    public T exchange(String port, String endPoint, UUID id, String requestParams, Class<T> classType) {
        try {
            HttpHeaders headers = new HttpHeaders();

            headers.add("jwt-secret", jwtSecret);

            String httpUrl = "http://localhost:" + port + "/" + endPoint + "/" + id;

            HttpEntity<String> entity = new HttpEntity<String>(headers);

            ResponseEntity<T> restResponseObject = restTemplate.exchange(httpUrl, HttpMethod.GET, entity, classType);

            return restResponseObject.getBody();
        } catch (Exception e) {
            throw e;
        }
    }
}