package com.events.events.services;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.events.events.domains.responseMessage.ResponseMessage;
import com.events.events.domains.training.Training;
import com.events.events.domains.training.TrainingDTO;
import com.events.events.repositories.TrainingRepository;

@SuppressWarnings("rawtypes")
@Service
public class TrainingService {
    private final TrainingRepository repo;

    public TrainingService(TrainingRepository repo) {
        this.repo = repo;
    }

    public ResponseEntity<ResponseMessage> register(TrainingDTO dto) {
        if (!checkTrainingDontExists(dto.teamId(), dto.inicialDateTime(), dto.finalDateTime()))
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ResponseMessage<>("Treino já cadastrado para este horário"));

        try {
            Training newTraining = new Training(dto);

            repo.save(newTraining);

            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage<Training>(newTraining));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseMessage<>("Erro ao cadastrar treino", e.getMessage()));
        }
    }

    public Boolean checkTrainingDontExists(UUID teamId, LocalDateTime inicialDatetime, LocalDateTime finalDatetime) {
        return !repo.findTrainingByTeamAndDate(teamId, inicialDatetime, finalDatetime).isPresent();
    }
}
