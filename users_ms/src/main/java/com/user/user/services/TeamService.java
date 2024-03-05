package com.user.user.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.user.user.domains.responseMessage.ResponseMessage;
import com.user.user.domains.team.Team;
import com.user.user.domains.team.TeamDTO;
import com.user.user.repositories.CoachRepository;
import com.user.user.repositories.TeamRepository;

@SuppressWarnings("rawtypes")
@Service
public class TeamService {
    @Autowired
    TeamRepository repo;

    @Autowired
    CoachRepository coachRepo;

    public ResponseEntity<ResponseMessage> register(TeamDTO dto) {
        Team newTeam = new Team(dto);

        if (!checkFieldsNotNull(newTeam)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseMessage<>("Verifique as credenciais do time"));
        }

        if (!checkCoach(newTeam.getCoach().getId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseMessage<>("Coach não encontrado"));
        }

        repo.save(newTeam);

        return ResponseEntity.ok(new ResponseMessage<Team>(newTeam));
    }

    public Boolean checkCoach(String coachId) {
        return !coachRepo.findById(coachId).isEmpty();
    }

    public Boolean checkFieldsNotNull(Team team) {
        return team.getCategory() != null &&
                team.getCoach() != null
                && team.getName() != null;
    }

    public ResponseEntity<ResponseMessage> getAllTeams() {
        return ResponseEntity.ok(new ResponseMessage<List<Team>>(repo.findAll()));
    }
}
