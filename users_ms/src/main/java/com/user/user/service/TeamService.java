package com.user.user.service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import org.springframework.http.ResponseEntity;


import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.user.user.domain.athlete.Athlete;
import com.user.user.domain.athlete.AthletewDesc;
import com.user.user.domain.athlete.InjuredAthleteDTO;
import com.user.user.domain.athleteDesc.AthleteDesc;
import com.user.user.domain.athleteHistoric.AthleteHistoric;
import com.user.user.domain.athleteHistoric.ForecastDTO;
import com.user.user.domain.coach.Coach;
import com.user.user.domain.gameResult.InGameForecastDTO;
import com.user.user.domain.injury.Injury;
import com.user.user.domain.operationCodes.OperationCode;
import com.user.user.domain.responseMessage.ResponseMessage;
import com.user.user.domain.team.ChangeTeamOwnerAcceptDTO;
import com.user.user.domain.team.ChangeTeamOwnerRequestDTO;
import com.user.user.domain.team.Team;
import com.user.user.domain.team.TeamDTO;
import com.user.user.exception.ResourceNotFoundException;
import com.user.user.repository.AthleteDescRepository;
import com.user.user.repository.AthleteHistoricRepository;
import com.user.user.repository.AthleteRepository;
import com.user.user.repository.CoachRepository;
import com.user.user.repository.OperationCodeRepository;
import com.user.user.repository.TeamRepository;
import com.user.user.util.CodeGenerator;
import com.user.user.util.CsvGenerator;
import com.user.user.util.Sorts;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository repo;
    private final CoachRepository coachRepo;
    private final AthleteRepository athleteRepo;
    private final OperationCodeRepository operationCodeRepo;
    private final AthleteDescRepository athleteDescRepo;
    private final AzureBlobService azureBlobService;
    private final AthleteHistoricRepository athleteHistoricRepository;

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

        return ResponseEntity.ok(new ResponseMessage<>(newTeam));
    }

    public ResponseEntity<ResponseMessage<Team>> getTeamById(UUID id) {
        return ResponseEntity.ok(new ResponseMessage<>(repo.findById(id).get()));
    }

    public ResponseEntity<?> generateCSV(UUID teamId) throws IOException {
        List<Athlete> athletes = athleteRepo.findAllByTeam_Id(teamId);

        List<AthletewDesc> dtoList = new ArrayList<>();

        for (Athlete athlete : athletes) {
            AthleteDesc athleteDesc = athleteDescRepo.findByAthleteId(athlete.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Informações do atleta", athlete.getId()));

            dtoList.add(new AthletewDesc(athlete, athleteDesc));
        }

        String csvPath = CsvGenerator.exportAthleteToCsv(dtoList, teamId);

        String blobURL = azureBlobService.uploadCSVFile(teamId.toString() + LocalDateTime.now(), csvPath);

        return ResponseEntity.status(200).body(blobURL);
    }

    public ResponseEntity<ResponseMessage<List<InjuredAthleteDTO>>> getActiveInjuriesOnTeam(UUID id,
            LocalDate nowDate) {
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

        return ResponseEntity.ok(new ResponseMessage<>(injuredAthletes));
    }

    public List<Team> findByCoachId(UUID coachId) {
        return repo.findByCoachId(coachId);
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

        return ResponseEntity.status(200).body(new ResponseMessage<>(athletes));
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

        if (dto.category() == null || dto.category().equals("")) {
            errors.add("Campo categoria é obrigatório");
        }

        if (dto.coach().getId() == null) {
            errors.add("Campo treinador -> id é obrigatório");
        }

        if (dto.name() == null || dto.name().equals("")) {
            errors.add("Campo nome é obrigatório");
        }

        return errors;
    }

    public ResponseEntity<ResponseMessage<List<Team>>> getAllTeams() {
        return ResponseEntity.status(200).body(new ResponseMessage<>(repo.findAll()));
    }

    public Team getTeamByAthlete(UUID athleteId) {
        Athlete athleteFound = athleteRepo.findById(athleteId)
                .orElseThrow(() -> new ResourceNotFoundException("Atleta", athleteId));

        return repo.findById(athleteFound.getTeam().getId())
                .orElseThrow(() -> new ResourceNotFoundException("time", athleteFound.getTeam().getId()));
    }

    private final RestTemplateService<ForecastDTO> forecastService;

    public ForecastDTO generateForecast(UUID challengerId, UUID challengedId) throws Exception {
      
        List<AthleteHistoric> challengerHistorics = athleteHistoricRepository.findByTeamId(challengerId);
        List<AthleteHistoric> challengedHistorics = athleteHistoricRepository.findByTeamId(challengedId);

        
        Map<String, Object> params = new HashMap<>();
        params.put("challengerHistorics", challengerHistorics);
        params.put("challengedHistorics", challengedHistorics);

        
        ObjectMapper objectMapper = new ObjectMapper();

      
        objectMapper.registerModule(new JavaTimeModule());

        String jsonParams = objectMapper.writeValueAsString(params);
        
        return forecastService.postForEntity("5729", "generate-forecast", jsonParams, ForecastDTO.class);
        
    }



}