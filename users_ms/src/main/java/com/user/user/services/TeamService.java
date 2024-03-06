package com.user.user.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.user.user.domains.athlete.Athlete;
import com.user.user.domains.athlete.InjuredAthlete;
import com.user.user.domains.injury.Injury;
import com.user.user.domains.responseMessage.ResponseMessage;
import com.user.user.domains.team.RegisterAthleteDTO;
import com.user.user.domains.team.Team;
import com.user.user.domains.team.TeamDTO;
import com.user.user.repositories.AthleteRepository;
import com.user.user.repositories.CoachRepository;
import com.user.user.repositories.InjuryRepository;
import com.user.user.repositories.TeamRepository;

@SuppressWarnings("rawtypes")
@Service
public class TeamService {
    @Autowired
    TeamRepository repo;

    @Autowired
    CoachRepository coachRepo;

    @Autowired
    AthleteRepository athleteRepo;

    @Autowired
    InjuryRepository injuryRepo;

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

    public ResponseEntity<ResponseMessage> registerAthleteToTeam(RegisterAthleteDTO dto) {
        Optional<Athlete> athlete = athleteRepo.findById(dto.athlete().getId());

        if (!athlete.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage<>("Atleta não encontrado"));
        }

        athlete.get().setTeam(dto.team());

        athleteRepo.save(athlete.get());

        return ResponseEntity.ok(new ResponseMessage<>(
                "Atleta " + athlete.get().getLastName() + " cadastrado no time"));
    }

    public ResponseEntity<ResponseMessage> getTeamById(String id) {
        return ResponseEntity.ok(new ResponseMessage<Team>(repo.findById(id).get()));
    }

    public ResponseEntity<ResponseMessage> getActiveInjuriesOnTeam(String id, LocalDate nowDate) {
        List<Athlete> athletes = athleteRepo.findByTeamId(id);

        List<InjuredAthlete> injuredAthletes = new ArrayList<>();

        for (Athlete athlete : athletes) {
            List<Injury> injuries = athlete.getInjuries();

            for (Injury injury : injuries) {
                if (nowDate.isAfter(injury.getInicialDate()) && nowDate.isBefore(injury.getFinalDate())) {
                    injuredAthletes.add(new InjuredAthlete(athlete, injury));
                }
            }
        }

        if (injuredAthletes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ResponseMessage<>("Sem jogadores lesionados"));
        }

        return ResponseEntity.ok(new ResponseMessage<List<InjuredAthlete>>(injuredAthletes));
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
