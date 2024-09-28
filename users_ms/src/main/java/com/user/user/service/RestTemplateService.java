package com.user.user.service;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.user.user.domain.athleteHistoric.AthleteHistoric;

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
        } catch (RestClientException e) {
            throw e;
        }
    }

    public T postForEntity(String port, String endPoint, Object params, Class<T> classType) throws Exception {
        try {
            T restResponseEntity = (T) restTemplate.postForEntity("http://localhost:" + port + "/" + endPoint, params, classType);

            return restResponseEntity;
        } catch (RestClientException e) {
            throw e;
        }
    }

    public T[] getTemplateList(String port, String endPoint, UUID id, Class<T[]> classType) throws Exception {
        try {
            ResponseEntity<T[]> restResponseEntity = restTemplate.getForEntity(
                    "http://localhost:" + port + "/" + endPoint + "/" + id, classType);

            return restResponseEntity.getBody();
        } catch (RestClientException e) {
            throw e;
        }
    }
}