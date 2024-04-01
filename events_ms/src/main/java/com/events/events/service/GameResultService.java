package com.events.events.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.events.events.domain.gameResult.GameResult;
import com.events.events.domain.gameResult.GameResultDTO;
import com.events.events.domain.responseMessage.ResponseMessage;
import com.events.events.repository.GameResultRepository;

@Service
public class GameResultService {
    private final GameResultRepository repo;

    public GameResultService(GameResultRepository repo) {
        this.repo = repo;
    }

    public ResponseEntity<ResponseMessage<GameResult>> register(GameResultDTO dto) {
        List<String> validateErrors = validateDTO(dto);

        if (!validateErrors.isEmpty()) {
            return ResponseEntity.status(400).body(new ResponseMessage<GameResult>(validateErrors.get(0)));
        }

        GameResult gameResult = new GameResult();

        BeanUtils.copyProperties(dto, gameResult);

        repo.save(gameResult);

        return ResponseEntity.status(201).body(new ResponseMessage<GameResult>(gameResult));
    }

    public List<String> validateDTO(GameResultDTO dto) {
        List<String> validateErrors = new ArrayList<>();

        if (dto.challengedPoints() < 0 || dto.challengerPoints() < 0) {
            validateErrors.add("Os pontos devem ser positivos");
        }

        return validateErrors;
    }
}