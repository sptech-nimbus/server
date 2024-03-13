package com.events.events.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.events.events.domains.coach.Coach;

@Service
public class CoachService {
    @Autowired
    private RestTemplate restTemplate;

    public Coach getCoachById(UUID coachId) throws Exception {
        try {
            ResponseEntity<Coach> coachResponseEntity = restTemplate.getForEntity(
                    "http://localhost:3000/coaches/ms-get-coach/" + coachId,
                    Coach.class);

            return coachResponseEntity.getBody();
        } catch (Exception e) {
            throw e;
        }
    }
}
