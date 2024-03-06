package com.user.user.services;

import java.util.Optional;

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
public class AthleteService extends PersonaService {
    @Autowired
    private AthleteRepository repo;

    public ResponseEntity<ResponseMessage> register(AthleteDTO dto, User user) {
        Athlete newAthlete = new Athlete(dto);
        newAthlete.setUser(user);

        if (!checkPersonaCredencials(newAthlete)
                || !checkCategory(newAthlete.getCategory())
                || !checkIsStarting(newAthlete.getIsStarting())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseMessage<>("Verifique suas credenciais de atleta"));
        }

        repo.save(newAthlete);

        return ResponseEntity
                .ok(new ResponseMessage<String>("Cadastro realizado", "Cadastro realizado", newAthlete.getId()));
    }

    public ResponseEntity<ResponseMessage> removeUserFromAthlete(String id) {
        Optional<Athlete> athleteFound = repo.findById(id);

        if (!athleteFound.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage<>("Atleta não encontrado."));
        }

        athleteFound.get().setUser(null);

        repo.save(athleteFound.get());

        return ResponseEntity.ok(new ResponseMessage<>("Usuário desvinculado de atleta"));
    }

    public Boolean checkCategory(String category) {
        return category != null;
    }

    public Boolean checkIsStarting(Boolean isStarting) {
        return isStarting != null;
    }
}