package com.user.user.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.user.user.domain.athlete.Athlete;
import com.user.user.domain.athlete.InjuredAthleteDTO;
import com.user.user.domain.coach.Coach;
import com.user.user.domain.injury.Injury;
import com.user.user.domain.operationCodes.OperationCode;
import com.user.user.domain.responseMessage.ResponseMessage;
import com.user.user.domain.team.ChangeTeamOwnerAcceptDTO;
import com.user.user.domain.team.ChangeTeamOwnerRequestDTO;
import com.user.user.domain.team.Team;
import com.user.user.domain.team.TeamDTO;
import com.user.user.exception.ResourceNotFoundException;
import com.user.user.repository.AthleteRepository;
import com.user.user.repository.CoachRepository;
import com.user.user.repository.OperationCodeRepository;
import com.user.user.repository.TeamRepository;
import com.user.user.util.CodeGenerator;
import com.user.user.util.Sorts;

@Service
public class TeamService {
    private final TeamRepository repo;
    private final CoachRepository coachRepo;
    private final AthleteRepository athleteRepo;
    private final OperationCodeRepository operationCodeRepo;

    public TeamService(TeamRepository repo, CoachRepository coachRepo, AthleteRepository athleteRepo,
            OperationCodeRepository operationCodeRepo) {
        this.repo = repo;
        this.coachRepo = coachRepo;
        this.athleteRepo = athleteRepo;
        this.operationCodeRepo = operationCodeRepo;
    }

    public ResponseEntity<ResponseMessage<Team>> register(TeamDTO dto) {
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

    public ResponseEntity<ResponseMessage<Team>> getTeamById(UUID id) {
        return ResponseEntity.ok(new ResponseMessage<Team>(repo.findById(id).get()));
    }

    public List<InjuredAthleteDTO> getActiveInjuriesOnTeam(UUID id, LocalDate nowDate) {
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

        return injuredAthletes;
    }

    public ResponseEntity<ResponseMessage<?>> putTeamById(UUID id, TeamDTO dto) {
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

    public ResponseEntity<ResponseMessage<List<Athlete>>> getAthletesByAgeAsc(UUID id) {
        Team teamFound = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Time", id));

        List<Athlete> athletes = teamFound.getAthletes();

        Sorts.mergeSortAthletesByAgeAsc(athletes, 0, athletes.size() - 1);

        return ResponseEntity.status(200).body(new ResponseMessage<List<Athlete>>(athletes));
    }

    public ResponseEntity<ResponseMessage<?>> changeTeamOwnerRequest(UUID id, ChangeTeamOwnerRequestDTO dto) {
        String code = CodeGenerator.codeGen(6, true);

        try {
            operationCodeRepo.save(new OperationCode("change-team-owner", code, dto.expirationDate(),
                    dto.mainUser(), dto.relatedUser()));
        } catch (Exception e) {
            return ResponseEntity.status(409).body(new ResponseMessage<>("Erro ao enviar código", e.getMessage()));
        }

        return ResponseEntity.status(200).body(new ResponseMessage<>("Código de troca posse de time: " + code, code));
    }

    public ResponseEntity<ResponseMessage<?>> changeTeamOwnerAccept(UUID id, ChangeTeamOwnerAcceptDTO dto) {
        OperationCode operationCodeFound = operationCodeRepo.findByCode(dto.code())
                .orElseThrow(() -> new ResourceNotFoundException("Código", null));

        if (dto.date().isAfter(operationCodeFound.getExpirationDate())) {
            return ResponseEntity.status(401).body(new ResponseMessage<>("Código expirado"));
        }

        Team teamFound = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Time", id));

        Coach coachFound = coachRepo.findCoachByUserId(operationCodeFound.getRelatedUser().getId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Treinador", operationCodeFound.getRelatedUser().getId()));

        teamFound.setCoach(coachFound);

        repo.save(teamFound);

        return ResponseEntity.status(200).body(new ResponseMessage<>(
                "Posse do time " + teamFound.getName() + " passado para o treinador " + coachFound.getLastName()));
    }

    public ResponseEntity<ResponseMessage<?>> deleteTeam(UUID id, String coachPassword) {
        Team teamFound = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Time", id));

        if (!teamFound.getCoach().getUser().getPassword().equals(coachPassword)) {
            return ResponseEntity.status(401).body(new ResponseMessage<>("Senha incorreta"));
        }

        athleteRepo.findByTeamId(id).stream().forEach(athlete -> athlete.setTeam(null));

        repo.delete(teamFound);

        return ResponseEntity.status(200).body(new ResponseMessage<>("Time excluído com sucesso"));
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

    public ResponseEntity<ResponseMessage<List<Team>>> getAllTeams() {
        return ResponseEntity.status(200).body(new ResponseMessage<List<Team>>(repo.findAll()));
    }

    public List<Team> getTeamsByName(String name) {
        List<Team> teamsFound = repo.findByNameContainsIgnoreCase(name);

        return teamsFound;
    }

    public List<Team> getTeamsByCoach(UUID userId) {
        Coach coachFound = coachRepo.findCoachByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Coach", userId));

        List<Team> teamsFound = repo.findByCoachId(coachFound.getId());

        return teamsFound;
    }
}