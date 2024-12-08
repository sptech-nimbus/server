package com.user.user.service;

import java.util.Collections;
import java.util.UUID;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;


import lombok.RequiredArgsConstructor;

import org.springframework.http.MediaType;

@Service
@RequiredArgsConstructor
public class RestTemplateService<T> {
    private final RestTemplate restTemplate;

    public T getTemplateById(String port, String endPoint, UUID id, Class<T> classType) throws Exception {
        try {
            ResponseEntity<T> restResponseEntity = restTemplate.getForEntity(
                    "http://localhost:" + port + "/" + endPoint + "/" + id, classType);

            return restResponseEntity.getBody();
        } catch (RestClientException e) {
            throw e;
        }
    }

    @SuppressWarnings("unchecked")
    public T postForEntity(String port, String endPoint, Object params, Class<T> classType) throws Exception {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Object> entity = new HttpEntity<>(params, headers);

            
            ResponseEntity<T> responseEntity = restTemplate.exchange(
                    "http://localhost:" + port + "/" + endPoint,
                    HttpMethod.POST,
                    entity,
                    classType
            );

            System.out.println("responseEntity body: " + responseEntity.getBody());

            
            return responseEntity.getBody();
        } catch (RestClientException e) {
            System.out.println("Exception" + e);
            throw new Exception("Erro ao enviar a requisição", e);
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