package com.user.user.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.user.user.models.athlete.Athlete;
import com.user.user.models.athlete.AthleteDTO;
import com.user.user.models.responseMessage.ResponseMessage;
import com.user.user.models.user.User;
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

    public Boolean checkCategory(String category) {
        return category != null;
    }

    public Boolean checkIsStarting(Boolean isStarting) {
        return isStarting != null;
    }
}