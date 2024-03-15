package com.user.user.services;

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
import com.user.user.domains.athlete.AthleteDTO;
import com.user.user.domains.responseMessage.ResponseMessage;
import com.user.user.domains.user.User;
import com.user.user.repositories.AthleteRepository;

@SuppressWarnings("rawtypes")
@Service
public class AthleteService extends PersonaService implements _persona<AthleteDTO> {
    @Autowired
    private AthleteRepository repo;

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
        Optional<Athlete> athleteFound = repo.findById(id);

        if (!athleteFound.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage<>("Atleta não encontrado."));
        }

        athleteFound.get().setUser(null);

        repo.save(athleteFound.get());

        return ResponseEntity.ok(new ResponseMessage<>("Usuário desvinculado de atleta"));
    }

    public ResponseEntity<Athlete> getAthleteForMs(UUID id) {
        Optional<Athlete> athleteFound = repo.findById(id);
        System.out.println(athleteFound.get());

        if (!athleteFound.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(athleteFound.get());
    }

    @Override
    public ResponseEntity<ResponseMessage> putPersona(UUID id, AthleteDTO dto) {
        Optional<Athlete> athleteFound = repo.findById(id);

        if (!athleteFound.isPresent()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ResponseMessage<>("Atleta não encontrado."));
        }

        BeanUtils.copyProperties(dto, athleteFound.get());

        try {
            repo.save(athleteFound.get());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseMessage<>("Erro ao atualizar atleta.", e.getMessage()));
        }

        return ResponseEntity.ok(new ResponseMessage<>("Atleta atualizado com sucesso"));
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