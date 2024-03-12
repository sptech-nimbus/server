package com.events.events.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.events.events.domains.responseMessage.ResponseMessage;

@SuppressWarnings("rawtypes")
@Service
public class TeamService {
    @Autowired
    private RestTemplate restTemplate;

    public Object getTeamInfoById(UUID teamId) {
        ResponseEntity<ResponseMessage> teamResponseEntity = restTemplate.getForEntity(
                "http://localhost:3000/teams/" + teamId,
                ResponseMessage.class);

        return teamResponseEntity.getBody().getData();
    }
}