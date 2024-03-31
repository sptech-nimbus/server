package com.events.events.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.events.events.domain.responseMessage.ResponseMessage;
import com.events.events.domain.training.Training;
import com.events.events.domain.training.TrainingDTO;
import com.events.events.exception.ResourceNotFoundException;
import com.events.events.repository.TrainingRepository;

@SuppressWarnings("rawtypes")
@Service
public class TrainingService {
    private final TrainingRepository repo;
    private final SimpMessagingTemplate wsMsgTemplate;

    public TrainingService(TrainingRepository repo, SimpMessagingTemplate wsMsgTemplate) {
        this.repo = repo;
        this.wsMsgTemplate = wsMsgTemplate;
    }

    public ResponseEntity<ResponseMessage> register(TrainingDTO dto) {
        if (!checkTrainingDontExists(dto))
            return ResponseEntity.status(409)
                    .body(new ResponseMessage("Treino já cadastrado para este horário"));

        Training newTraining = new Training(dto);

        try {
            repo.save(newTraining);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(new ResponseMessage("Erro ao cadastrar treino", e.getMessage()));
        }

        ResponseMessage response = new ResponseMessage<Training>(newTraining);

        wsMsgTemplate.convertAndSend("/events/" + newTraining.getTeam(), response);

        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ResponseMessage> getTrainingsPageByTeamId(UUID teamId, Integer page, Integer elements) {
        Pageable pageable = PageRequest.of(page, elements);

        Page<Training> trainingsFound = repo.findAllByTeam(teamId, pageable);

        if (!trainingsFound.hasContent())
            return ResponseEntity.status(204).body(new ResponseMessage("Time sem treinos marcados"));

        return ResponseEntity.status(200).body(new ResponseMessage<Page<Training>>(trainingsFound));
    }

    public ResponseEntity<ResponseMessage> putTraining(UUID id, TrainingDTO dto) {
        if (!checkTrainingDontExists(dto, id))
            return ResponseEntity.status(409)
                    .body(new ResponseMessage("Treino já cadastrado para este horário"));

        Training trainingFound = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Treino", id));

        BeanUtils.copyProperties(dto, trainingFound);

        try {
            repo.save(trainingFound);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(new ResponseMessage("Erro ao atualizar treino", e.getMessage()));
        }

        return ResponseEntity.status(200).body(new ResponseMessage<Training>(trainingFound));
    }

    public ResponseEntity<ResponseMessage> deleteTraining(UUID id) {
        Training trainingFound = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Treino", id));

        try {
            repo.delete(trainingFound);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseMessage("Erro ao deletar treino", e.getMessage()));
        }

        return ResponseEntity.status(200).body(new ResponseMessage("Treino deletado"));
    }

    public Boolean checkTrainingDontExists(TrainingDTO dto) {
        return !repo.findByTeamIdAndFinalDateTimeBetween(dto.teamId(), dto.inicialDateTime(), dto.finalDateTime())
                .isPresent();
    }

    public Boolean checkTrainingDontExists(TrainingDTO dto, UUID id) {
        Optional<Training> trainingFinal = repo.findByTeamIdAndFinalDateTimeBetween(dto.teamId(), dto.inicialDateTime(),
                dto.finalDateTime());

        return !trainingFinal.isPresent() || trainingFinal.get().getId().equals(id);
    }
}
