package com.user.user.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
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
import com.user.user.repositories.TeamRepository;
import com.user.user.utils.Sorts;

@SuppressWarnings("rawtypes")
@Service
public class TeamService {
    private final TeamRepository repo;
    private final CoachRepository coachRepo;
    private final AthleteRepository athleteRepo;

    public TeamService(TeamRepository repo, CoachRepository coachRepo, AthleteRepository athleteRepo) {
        this.repo = repo;
        this.coachRepo = coachRepo;
        this.athleteRepo = athleteRepo;
    }

    public ResponseEntity<ResponseMessage> register(TeamDTO dto) {
        List<String> fieldsErrors = checkFields(dto);

        if (!fieldsErrors.isEmpty()) {
            return ResponseEntity.status(400)
                    .body(new ResponseMessage<>(String.join(", ", fieldsErrors)));
        }

        coachRepo.findById(dto.coach().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Treinador", dto.coach().getId()));

        Team newTeam = new Team();

        BeanUtils.copyProperties(dto, newTeam);

        repo.save(newTeam);

        return ResponseEntity.ok(new ResponseMessage<Team>(newTeam));
    }

    public ResponseEntity<ResponseMessage> getTeamById(UUID id) {
        return ResponseEntity.ok(new ResponseMessage<Team>(repo.findById(id).get()));
    }

    public ResponseEntity<ResponseMessage> getActiveInjuriesOnTeam(UUID id, LocalDate nowDate) {
        Team team = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Time", id));

        List<InjuredAthleteDTO> injuredAthletes = new ArrayList<>();

        for (Athlete athlete : team.getAthletes()) {
            List<Injury> injuries = athlete.getInjuries();

            for (Injury injury : injuries) {
                if (nowDate.isAfter(injury.getInicialDate()) &&
                        nowDate.isBefore(injury.getFinalDate())) {
                    injuredAthletes.add(new InjuredAthleteDTO(athlete, injury));
                }
            }
        }

        if (injuredAthletes.isEmpty()) {
            return ResponseEntity.status(204).body(new ResponseMessage<>("Sem jogadores lesionados"));
        }

        return ResponseEntity.ok(new ResponseMessage<List<InjuredAthleteDTO>>(injuredAthletes));
    }

    public ResponseEntity<ResponseMessage> putTeamById(UUID id, TeamDTO dto) {
        Team team = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Time", id));

        BeanUtils.copyProperties(dto, team);

        try {
            repo.save(team);
        } catch (Exception e) {
            throw e;
        }

        return ResponseEntity.status(200).body(new ResponseMessage<>("Time atualizado"));
    }

    public ResponseEntity<Team> getTeamByIdForMs(UUID id) {
        Team teamFound = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Time", id));

        return ResponseEntity.status(200).body(teamFound);
    }

    public ResponseEntity<ResponseMessage> getAthletesByAgeAsc(UUID id) {
        Team teamFound = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Time", id));

        List<Athlete> athletes = teamFound.getAthletes();

        Sorts.mergeSortAthletesByAgeAsc(athletes, 0, athletes.size() - 1);

        return ResponseEntity.status(200).body(new ResponseMessage<List<Athlete>>(athletes));
    }

    public ResponseEntity<ResponseMessage> deleteTeam(UUID id, String coachPassword) {
        Team teamFound = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Time", id));

        if (!teamFound.getCoach().getUser().getPassword().equals(coachPassword)) {
            return ResponseEntity.status(401).body(new ResponseMessage("Senha incorreta"));
        }

        athleteRepo.findByTeamId(id).stream().forEach(athlete -> athlete.setTeam(null));

        repo.delete(teamFound);

        return ResponseEntity.status(200).body(new ResponseMessage("Time excluído com sucesso"));
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
        return ResponseEntity.status(200).body(new ResponseMessage<List<Team>>(repo.findAll()));
    }
}