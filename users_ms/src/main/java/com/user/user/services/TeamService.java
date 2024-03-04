package com.user.user.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.user.user.models.responseMessage.ResponseMessage;
import com.user.user.models.team.Team;
import com.user.user.models.team.TeamDTO;
import com.user.user.repositories.TeamRepository;

@SuppressWarnings("rawtypes")
@Service
public class TeamService {
    @Autowired
    TeamRepository repo;

    public ResponseEntity<ResponseMessage> register(TeamDTO dto) {
        Team newTeam = new Team(dto);

        repo.save(newTeam);

        return ResponseEntity.ok(new ResponseMessage<Team>(newTeam));
    }

    public ResponseEntity<ResponseMessage> getAllTeams() {
        return ResponseEntity.ok(new ResponseMessage<List<Team>>(repo.findAll()));
    }
}
