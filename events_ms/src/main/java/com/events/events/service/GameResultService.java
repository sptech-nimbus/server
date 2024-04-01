package com.events.events.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.events.events.domain.coach.Coach;
import com.events.events.domain.gameResult.GameResult;
import com.events.events.domain.gameResult.GameResultDTO;
import com.events.events.domain.responseMessage.ResponseMessage;
import com.events.events.exception.ResourceNotFoundException;
import com.events.events.repository.GameResultRepository;

@Service
public class GameResultService {
    private final GameResultRepository repo;
    private final RestTemplateService<Coach> coachService;

    public GameResultService(GameResultRepository repo, RestTemplateService<Coach> coachService) {
        this.repo = repo;
        this.coachService = coachService;
    }

    public ResponseEntity<ResponseMessage<GameResult>> register(GameResultDTO dto) {
        List<String> validateErrors = validateDTO(dto);

        if (!validateErrors.isEmpty()) {
            return ResponseEntity.status(400).body(new ResponseMessage<GameResult>(validateErrors.get(0)));
        }

        GameResult gameResult = new GameResult();

        BeanUtils.copyProperties(dto, gameResult);

        gameResult.setConfirmed(false);

        repo.save(gameResult);

        return ResponseEntity.status(201).body(new ResponseMessage<GameResult>(gameResult));
    }

    public ResponseEntity<ResponseMessage<GameResult>> confirmGameResult(UUID id, Coach coach) {
        Coach coachFound;

        try {
            coachFound = coachService.getTemplateById("3000", "coaches/ms-get-coach", coach.getId(), Coach.class);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ResponseMessage<GameResult>(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseMessage<GameResult>(e.getMessage()));
        }

        GameResult gameResult = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resultado de jogo", id));

        if (!coachFound.getTeams().stream()
                .anyMatch(team -> team.getId().equals(gameResult.getGame().getChallenged()))) {
            return ResponseEntity.status(409).body(new ResponseMessage<GameResult>(
                    "Apenas o treinador do time desafiado pode confirmar um resultado"));
        }

        gameResult.setConfirmed(true);

        repo.save(gameResult);

        return ResponseEntity.status(200).body(new ResponseMessage<GameResult>(gameResult));
    }

    public List<String> validateDTO(GameResultDTO dto) {
        List<String> validateErrors = new ArrayList<>();

        if (dto.challengedPoints() < 0 || dto.challengerPoints() < 0) {
            validateErrors.add("Os pontos devem ser positivos");
        }

        return validateErrors;
    }
}