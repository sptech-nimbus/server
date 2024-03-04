package com.user.user.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.user.user.models.coach.Coach;
import com.user.user.models.coach.CoachDTO;
import com.user.user.models.responseMessage.ResponseMessage;
import com.user.user.models.user.User;
import com.user.user.repositories.CoachRepository;

@SuppressWarnings("rawtypes")
@Service
public class CoachService extends PersonaService {
    @Autowired
    private CoachRepository repo;

    public ResponseEntity<ResponseMessage> register(CoachDTO dto, User user) {
        Coach newCoach = new Coach(dto);
        newCoach.setUser(user);

        if (!checkPersonaCredencials(newCoach)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseMessage<>("Verifique suas credenciais de treinador"));
        }

        repo.save(newCoach);

        return ResponseEntity
                .ok(new ResponseMessage<String>("Cadastro realizado", "Cadastro realizado", newCoach.getId()));
    }
}
