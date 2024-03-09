package com.user.user.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.user.user.domains.coach.Coach;
import com.user.user.domains.coach.CoachDTO;
import com.user.user.domains.responseMessage.ResponseMessage;
import com.user.user.domains.user.User;
import com.user.user.repositories.CoachRepository;

@SuppressWarnings("rawtypes")
@Service
public class CoachService extends PersonaService implements _persona<CoachDTO> {
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

    public ResponseEntity<ResponseMessage> removeUserFromCoach(String id) {
        Optional<Coach> coachFound = repo.findById(id);

        if (!coachFound.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage<>("Treinador não encontrado."));
        }

        coachFound.get().setUser(null);

        repo.save(coachFound.get());

        return ResponseEntity.ok(new ResponseMessage<>("Usuário desvinculado de Treinador"));
    }

    @Override
    public ResponseEntity<ResponseMessage> putPersona(String id, CoachDTO dto) {
        
        return null;
    }
}
