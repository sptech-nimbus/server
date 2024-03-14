package com.user.user.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.user.user.domains.coach.Coach;
import com.user.user.domains.coach.CoachDTO;
import com.user.user.domains.coach.sCoachDTO;
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
                .ok(new ResponseMessage<UUID>("Cadastro realizado", "Cadastro realizado", newCoach.getId()));
    }

    public ResponseEntity<ResponseMessage> removeUserFromCoach(UUID id) {
        Optional<Coach> coachFound = repo.findById(id);

        if (!coachFound.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage<>("Treinador não encontrado."));
        }

        coachFound.get().setUser(null);

        repo.save(coachFound.get());

        return ResponseEntity.ok(new ResponseMessage<>("Usuário desvinculado de Treinador"));
    }

    public ResponseEntity<sCoachDTO> getCoachById(UUID id) {
        Optional<Coach> coachFound = repo.findById(id);

        if (!coachFound.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(new sCoachDTO(coachFound.get()));
    }

    @Override
    public ResponseEntity<ResponseMessage> putPersona(UUID id, CoachDTO dto) {
        Optional<Coach> coachFound = repo.findById(id);

        if (!coachFound.isPresent()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(new ResponseMessage<>("Treinador não encontrado."));
        }

        BeanUtils.copyProperties(dto, coachFound.get());

        try {
            repo.save(coachFound.get());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseMessage<>("Erro ao atualizar treinador.", e.getMessage()));
        }

        return ResponseEntity.ok(new ResponseMessage<>("Treinador atualizado com sucesso"));
    }
}
