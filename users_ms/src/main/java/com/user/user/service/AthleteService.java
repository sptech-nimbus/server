package com.user.user.service;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.user.user.domain.athlete.Athlete;
import com.user.user.domain.athlete.AthleteDTO;
import com.user.user.domain.athlete.AthletewDesc;
import com.user.user.domain.athleteDesc.AthleteDesc;
import com.user.user.domain.responseMessage.ResponseMessage;
import com.user.user.domain.team.Team;
import com.user.user.domain.user.User;
import com.user.user.exception.ResourceNotFoundException;
import com.user.user.repository.AthleteDescRepository;
import com.user.user.repository.AthleteRepository;
import com.user.user.repository.TeamRepository;
import com.user.user.util.CsvGenerator;

@Service
public class AthleteService extends PersonaService implements _persona<AthleteDTO> {
    private final AthleteRepository repo;
    private final TeamRepository teamRepo;
    private final AthleteDescRepository athleteDescRepo;

    public AthleteService(AthleteRepository repo, TeamRepository teamRepo, AthleteDescRepository athleteDescRepo) {
        super(repo, null);
        this.repo = repo;
        this.teamRepo = teamRepo;
        this.athleteDescRepo = athleteDescRepo;
    }

    public ResponseEntity<ResponseMessage<UUID>> register(AthleteDTO dto, User user) {
        Athlete newAthlete = new Athlete();

        BeanUtils.copyProperties(dto, newAthlete);

        newAthlete.setUser(user);

        if (!checkPersonaCredencials(newAthlete)
                || !checkAthleteCredentials(dto.category(), dto.isStarting()).isEmpty()) {
            return ResponseEntity.status(400)
                    .body(new ResponseMessage<>("Verifique suas credenciais de atleta"));
        }

        repo.save(newAthlete);

        return ResponseEntity.status(201).body(new ResponseMessage<UUID>("Cadastro realizado", newAthlete.getId()));
    }

    public ResponseEntity<ResponseMessage<?>> removeUserFromAthlete(UUID id) {
        Athlete athleteFound = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Atleta", id));

        athleteFound.setUser(null);

        repo.save(athleteFound);

        return ResponseEntity.status(200).body(new ResponseMessage<>("Usuário desvinculado de atleta"));
    }

    public ResponseEntity<Athlete> getAthleteForMs(UUID id) {
        Athlete athleteFound = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Atleta", id));

        return ResponseEntity.status(200).body(athleteFound);
    }

    @Override
    public ResponseEntity<ResponseMessage<?>> putPersona(UUID id, AthleteDTO dto) {
        Athlete athleteFound = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Atleta", id));

        BeanUtils.copyProperties(dto, athleteFound);

        try {
            repo.save(athleteFound);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(new ResponseMessage<>("Erro ao atualizar atleta.", e.getMessage()));
        }

        return ResponseEntity.status(200).body(new ResponseMessage<>("Atleta atualizado com sucesso"));
    }

    public ResponseEntity<ResponseMessage<?>> registerAthleteToTeam(UUID id, Team team) {
        System.out.println(team.getId());
        Team teamFound = teamRepo.findById(team.getId()).orElseThrow(() -> new ResourceNotFoundException("Time", id));

        Athlete athleteFound = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Atleta", id));

        athleteFound.setTeam(teamFound);

        repo.save(athleteFound);

        return ResponseEntity.status(200)
                .body(new ResponseMessage<>("Atleta " + athleteFound.getLastName() + " cadastrado no time"));
    }

    public List<String> checkAthleteCredentials(String category, Boolean isStarting) {
        List<String> errors = new ArrayList<>();

        if (category == null || category == "") {
            errors.add("Campo categoria é obrigatório");
        }

        if (isStarting == null) {
            errors.add("Campo titular é obrigatório");
        }

        return errors;
    }

    public ResponseEntity<?> generateCSV(List<UUID> ids) {
        List<Athlete> athletes = repo.findAllById(ids);

        List<AthletewDesc> dtoList = new ArrayList<>();

        for (Athlete athlete : athletes) {
            AthleteDesc athleteDesc = athleteDescRepo.findByAthleteId(athlete.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Informações do atleta", athlete.getId()));

            dtoList.add(new AthletewDesc(athlete, athleteDesc));
        }

        CsvGenerator.gravaArquivoCsv(dtoList, "athletes-" + LocalDate.now().toString());

        return ResponseEntity.status(200).build();
    }
}