package com.user.user.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.user.user.domain.athleteHistoric.AthleteHistoric;
import com.user.user.domain.athleteHistoric.AthleteHistoricDTO;
import com.user.user.domain.game.Game;
import com.user.user.domain.responseMessage.ResponseMessage;
import com.user.user.domain.training.Training;
import com.user.user.exception.ResourceNotFoundException;
import com.user.user.repository.AthleteHistoricRepository;
import com.user.user.repository.AthleteRepository;

@Service
public class AthleteHistoricService {
    private final AthleteHistoricRepository repo;
    private final AthleteRepository athleteRepo;
    private final RestTemplateService<Game> gameService;
    private final RestTemplateService<Training> trainingService;

    public AthleteHistoricService(AthleteHistoricRepository repo, AthleteRepository athleteRepo,
            RestTemplateService<Game> gameService, RestTemplateService<Training> trainingService) {
        this.repo = repo;
        this.athleteRepo = athleteRepo;
        this.gameService = gameService;
        this.trainingService = trainingService;
    }

    public ResponseEntity<ResponseMessage<AthleteHistoric>> register(AthleteHistoricDTO dto) {
        athleteRepo.findById(dto.athlete().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Atleta", dto.athlete().getId()));

        AthleteHistoric newAthleteHistoric = new AthleteHistoric();

        BeanUtils.copyProperties(dto, newAthleteHistoric);

        try {
            if (dto.game() != null) {
                Game gameFound = gameService.getTemplateById("3002", "/games/ms-get-by-id", dto.game().getId(),
                        Game.class);

                newAthleteHistoric.setGameId(gameFound.getId());
            } else if (dto.training() != null) {
                Training trainingFound = trainingService.getTemplateById("3002", "/trainings/ms-get-by-id",
                        dto.training().getId(),
                        Training.class);

                newAthleteHistoric.setTrainingId(trainingFound.getId());
            } else {
                return ResponseEntity.status(400).body(
                        new ResponseMessage<>("Informe o jogo ou treino relacionado com este histórico de jogador"));
            }
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ResponseMessage<>(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(new ResponseMessage<>("Serviço de eventos fora do ar no momento", e.getMessage()));
        }

        try {
            repo.save(newAthleteHistoric);
        } catch (Exception e) {
            return ResponseEntity.status(400)
                    .body(new ResponseMessage<AthleteHistoric>("Erro ao registrar historico de atleta",
                            e.getMessage()));
        }

        return ResponseEntity.status(201).body(new ResponseMessage<AthleteHistoric>(newAthleteHistoric));
    }

    public ResponseEntity<ResponseMessage<List<AthleteHistoric>>> getAthleteHistoricsByAthleteId(UUID athleteId) {
        List<AthleteHistoric> athleteHistoricFound = repo.findByAthleteId(athleteId);

        if (athleteHistoricFound.isEmpty())
            return ResponseEntity.status(204)
                    .body(new ResponseMessage<List<AthleteHistoric>>("Nenhum histórico do atleta de id " + athleteId));

        return ResponseEntity.status(200).body(new ResponseMessage<List<AthleteHistoric>>(athleteHistoricFound));
    }

    public ResponseEntity<ResponseMessage<Page<AthleteHistoric>>> getAthleteHistoricsPageByAthleteId(UUID athleteId,
            Integer page,
            Integer elements) {
        Page<AthleteHistoric> athleteHistoricsFound = repo.findAllByAthleteId(athleteId,
                PageRequest.of(page, elements));

        if (athleteHistoricsFound.isEmpty()) {
            return ResponseEntity.status(204)
                    .body(new ResponseMessage<Page<AthleteHistoric>>("Nenhum histórico do atleta de id " + athleteId));
        }

        return ResponseEntity.status(200).body(new ResponseMessage<Page<AthleteHistoric>>(athleteHistoricsFound));
    }

    public ResponseEntity<ResponseMessage<AthleteHistoric>> putAhlteteHistoric(UUID id, AthleteHistoricDTO dto) {
        AthleteHistoric athleteHistoricFound = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Histórico de atleta", id));

        BeanUtils.copyProperties(dto, athleteHistoricFound);

        repo.save(athleteHistoricFound);

        return ResponseEntity.status(200).body(new ResponseMessage<AthleteHistoric>("Histórico de atleta atualizado"));
    }

    public ResponseEntity<ResponseMessage<?>> deleteAhleteHistoric(UUID id) {
        AthleteHistoric athleteHistoricFound = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Histórico de atleta", id));

        repo.delete(athleteHistoricFound);

        return ResponseEntity.status(200).body(new ResponseMessage<>("Histórico de atleta deletado"));
    }

    public ResponseEntity<List<AthleteHistoric>> msGetByGameIdList(UUID teamId, List<UUID> gamesIdList) {
        List<AthleteHistoric> athleteHistoricsFound = repo.findByAthleteTeamIdAndGameIdIn(teamId, gamesIdList);
  
        return ResponseEntity.status(200).body(athleteHistoricsFound);
    }
}