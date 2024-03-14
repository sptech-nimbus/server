package com.user.user.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.user.user.domains.athlete.Athlete;
import com.user.user.domains.athlete.InjuredAthleteDTO;
import com.user.user.domains.injury.Injury;
import com.user.user.domains.responseMessage.ResponseMessage;
import com.user.user.domains.team.Team;
import com.user.user.domains.team.TeamDTO;
import com.user.user.exceptions.ResourceNotFoundException;
import com.user.user.repositories.AthleteRepository;
import com.user.user.repositories.CoachRepository;
import com.user.user.repositories.InjuryRepository;
import com.user.user.repositories.TeamRepository;
import com.user.user.utils.Sorts;

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
        List<String> fieldsErrors = checkFields(dto);
        if (!fieldsErrors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseMessage<>(String.join(", ", fieldsErrors)));
        }

        coachRepo.findById(dto.coach().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Treinador", dto.coach().getId()));

        Team newTeam = new Team();

        BeanUtils.copyProperties(dto, newTeam);

        repo.save(newTeam);

        return ResponseEntity.ok(new ResponseMessage<Team>(newTeam));
    }

    public ResponseEntity<ResponseMessage> registerAthleteToTeam(UUID id, Athlete athlete) {
        Team teamFound = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Time", id));

        Athlete athleteFound = athleteRepo.findById(athlete.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Atleta", athlete.getId()));

        athleteFound.setTeam(teamFound);

        athleteRepo.save(athleteFound);

        return ResponseEntity.status(200).body(new ResponseMessage(
                "Atleta " + athleteFound.getLastName() + " cadastrado no time"));
    }

    public ResponseEntity<ResponseMessage> getTeamById(UUID id) {
        return ResponseEntity.ok(new ResponseMessage<Team>(repo.findById(id).get()));
    }

    public ResponseEntity<ResponseMessage> getActiveInjuriesOnTeam(UUID id, LocalDate nowDate) {
        List<Athlete> athletes = athleteRepo.findByTeamId(id);

        List<InjuredAthleteDTO> injuredAthletes = new ArrayList<>();

        for (Athlete athlete : athletes) {
            List<Injury> injuries = athlete.getInjuries();

            for (Injury injury : injuries) {
                if (nowDate.isAfter(injury.getInicialDate()) && nowDate.isBefore(injury.getFinalDate())) {
                    injuredAthletes.add(new InjuredAthleteDTO(athlete, injury));
                }
            }
        }

        if (injuredAthletes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ResponseMessage<>("Sem jogadores lesionados"));
        }

        return ResponseEntity.ok(new ResponseMessage<List<InjuredAthleteDTO>>(injuredAthletes));
    }

    public ResponseEntity<ResponseMessage> putTeamById(UUID id, TeamDTO dto) {
        Optional<Team> team = repo.findById(id);

        if (!team.isPresent())
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ResponseMessage<>("Time não encontrado"));

        BeanUtils.copyProperties(dto, team.get());

        try {
            repo.save(team.get());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseMessage<>("Erro ao atualizar time", e.getMessage()));
        }

        return ResponseEntity.ok(new ResponseMessage<>("Time atualizado"));
    }

    public ResponseEntity<Team> getTeamByIdForMs(UUID id) {
        Optional<Team> teamFound = repo.findById(id);

        if (!teamFound.isPresent())
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        return ResponseEntity.status(200).body(teamFound.get());
    }

    public ResponseEntity<ResponseMessage> getAthletesByAgeAsc(UUID id) {
        Optional<Team> teamFound = repo.findById(id);

        if (!teamFound.isPresent())
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ResponseMessage<>("Time não encontrado"));

        List<Athlete> athletes = teamFound.get().getAthletes();

        System.out.println(athletes);

        Sorts.mergeSortAthletesByAgeAsc(athletes, athletes.size());

        return ResponseEntity.ok().body(new ResponseMessage<List<Athlete>>(athletes));
    }

    public List<String> checkFields(TeamDTO dto) {
        List<String> errors = new ArrayList<>();

        if (dto.category() == null || dto.category() == "") {
            errors.add("Campo categoria é obrigatório");
        }

        if (dto.coach().getId() == null) {
            errors.add("Campo treinador -> id é obrigatório");
        }

        if (dto.name() == null || dto.name() == "") {
            errors.add("Campo nome é obrigatório");
        }

        return errors;
    }

    public ResponseEntity<ResponseMessage> getAllTeams() {
        return ResponseEntity.ok(new ResponseMessage<List<Team>>(repo.findAll()));
    }
}