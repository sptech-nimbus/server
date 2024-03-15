package com.user.user.services;

import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.user.user.domains.coach.Coach;
import com.user.user.domains.coach.CoachDTO;
import com.user.user.domains.coach.sCoachDTO;
import com.user.user.domains.responseMessage.ResponseMessage;
import com.user.user.domains.user.User;
import com.user.user.exceptions.ResourceNotFoundException;
import com.user.user.repositories.CoachRepository;

@SuppressWarnings("rawtypes")
@Service
public class CoachService extends PersonaService implements _persona<CoachDTO> {
    @Autowired
    private CoachRepository repo;

    public ResponseEntity<ResponseMessage> register(CoachDTO dto, User user) {
        Coach newCoach = new Coach();

        BeanUtils.copyProperties(dto, newCoach);

        newCoach.setUser(user);

        if (!checkPersonaCredencials(newCoach)) {
            return ResponseEntity.status(400)
                    .body(new ResponseMessage("Verifique suas credenciais de treinador"));
        }

        repo.save(newCoach);

        return ResponseEntity
                .status(200)
                .body(new ResponseMessage<UUID>("Cadastro realizado", newCoach.getId()));
    }

    public ResponseEntity<ResponseMessage> removeUserFromCoach(UUID id) {
        Coach coachFound = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Treinador", id));

        coachFound.setUser(null);

        repo.save(coachFound);

        return ResponseEntity.status(200).body(new ResponseMessage("Usuário desvinculado de Treinador"));
    }

    public ResponseEntity<sCoachDTO> getCoachById(UUID id) {
        Coach coachFound = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Treinador", id));

        return ResponseEntity.status(200).body(new sCoachDTO(coachFound));
    }

    @Override
    public ResponseEntity<ResponseMessage> putPersona(UUID id, CoachDTO dto) {
        Coach coachFound = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Treinador", id));

        BeanUtils.copyProperties(dto, coachFound);

        try {
            repo.save(coachFound);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(new ResponseMessage("Erro ao atualizar treinador.", e.getMessage()));
        }

        return ResponseEntity.status(200).body(new ResponseMessage("Treinador atualizado com sucesso"));
    }
}
