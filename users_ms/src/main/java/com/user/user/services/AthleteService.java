package com.user.user.services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.user.user.domains.athlete.Athlete;
import com.user.user.domains.athlete.AthleteDTO;
import com.user.user.domains.responseMessage.ResponseMessage;
import com.user.user.domains.user.User;
import com.user.user.exceptions.ResourceNotFoundException;
import com.user.user.repositories.AthleteRepository;

@SuppressWarnings("rawtypes")
@Service
public class AthleteService extends PersonaService implements _persona<AthleteDTO> {
    private final AthleteRepository repo;

    @Autowired
    public AthleteService(AthleteRepository repo) {
        super(repo, null);
        this.repo = repo;
    }

    public ResponseEntity<ResponseMessage> register(AthleteDTO dto, User user) {
        Athlete newAthlete = new Athlete();

        BeanUtils.copyProperties(dto, newAthlete);

        newAthlete.setUser(user);

        if (!checkPersonaCredencials(newAthlete)
                || !checkAthleteCredentials(dto.category(), dto.isStarting()).isEmpty()) {
            return ResponseEntity.status(400)
                    .body(new ResponseMessage<>("Verifique suas credenciais de atleta"));
        }

        repo.save(newAthlete);

        return ResponseEntity
                .status(200)
                .body(new ResponseMessage<UUID>("Cadastro realizado", newAthlete.getId()));
    }

    public ResponseEntity<ResponseMessage> removeUserFromAthlete(UUID id) {
        Athlete athleteFound = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Atleta", id));

        athleteFound.setUser(null);

        repo.save(athleteFound);

        return ResponseEntity.ok(new ResponseMessage("Usuário desvinculado de atleta"));
    }

    public ResponseEntity<Athlete> getAthleteForMs(UUID id) {
        Athlete athleteFound = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Atleta", id));

        return ResponseEntity.status(200).body(athleteFound);
    }

    @Override
    public ResponseEntity<ResponseMessage> putPersona(UUID id, AthleteDTO dto) {
        Athlete athleteFound = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Atleta", id));

        BeanUtils.copyProperties(dto, athleteFound);

        try {
            repo.save(athleteFound);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(new ResponseMessage("Erro ao atualizar atleta.", e.getMessage()));
        }

        return ResponseEntity.ok(new ResponseMessage("Atleta atualizado com sucesso"));
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
}