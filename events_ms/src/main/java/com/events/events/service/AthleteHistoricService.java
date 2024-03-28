package com.events.events.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.events.events.domain.athlete.Athlete;
import com.events.events.domain.athleteHistoric.AthleteHistoric;
import com.events.events.domain.athleteHistoric.AthleteHistoricDTO;
import com.events.events.domain.responseMessage.ResponseMessage;
import com.events.events.exception.ResourceNotFoundException;
import com.events.events.repository.AthleteHistoricRepository;

@SuppressWarnings("rawtypes")
@Service
public class AthleteHistoricService {
    private final AthleteHistoricRepository repo;
    private final RestTemplateService<Athlete> athleteService;

    public AthleteHistoricService(AthleteHistoricRepository repo, RestTemplateService<Athlete> athleteService) {
        this.repo = repo;
        this.athleteService = athleteService;
    }

    public ResponseEntity<ResponseMessage> register(AthleteHistoricDTO dto) {
        Athlete athleteFound;

        try {
            athleteFound = athleteService.getTemplateById("3000", "athletes/ms-get-athlete", dto.athleteId(),
                    Athlete.class);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ResponseMessage(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(new ResponseMessage("Serviço de usuários fora do ar no momento", e.getMessage()));
        }

        AthleteHistoric newAthleteHistoric = new AthleteHistoric();

        BeanUtils.copyProperties(dto, newAthleteHistoric);

        newAthleteHistoric.setAthleteId(athleteFound.getId());

        try {
            repo.save(newAthleteHistoric);
        } catch (Exception e) {
            return ResponseEntity.status(400)
                    .body(new ResponseMessage("Erro ao registrar historico de atleta", e.getMessage()));
        }

        return ResponseEntity.status(201).body(new ResponseMessage<AthleteHistoric>(newAthleteHistoric));
    }

    public ResponseEntity<ResponseMessage> getAthleteHistoricsByAthleteId(UUID athleteId) {
        List<AthleteHistoric> athleteHistoricFound = repo.findByAthleteId(athleteId);

        if (athleteHistoricFound.isEmpty())
            return ResponseEntity.status(204)
                    .body(new ResponseMessage("Nenhum histórico do atleta de id " + athleteId));

        return ResponseEntity.status(200).body(new ResponseMessage<List<AthleteHistoric>>(athleteHistoricFound));
    }

    public ResponseEntity<ResponseMessage> getAthleteHistoricsPageByAthleteId(UUID athleteId, Integer page,
            Integer elements) {
        Page<AthleteHistoric> athleteHistoricsFound = repo.findAllByAthleteId(athleteId,
                PageRequest.of(page, elements));

        if (athleteHistoricsFound.isEmpty()) {
            return ResponseEntity.status(204)
                    .body(new ResponseMessage("Nenhum histórico do atleta de id " + athleteId));
        }

        return ResponseEntity.status(200).body(new ResponseMessage<Page<AthleteHistoric>>(athleteHistoricsFound));
    }

    public ResponseEntity<ResponseMessage> putAhlteteHistoric(UUID id, AthleteHistoricDTO dto) {
        AthleteHistoric athleteHistoricFound = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Histórico de atleta", id));

        BeanUtils.copyProperties(dto, athleteHistoricFound);

        repo.save(athleteHistoricFound);

        return ResponseEntity.status(200).body(new ResponseMessage("Histórico de atleta atualizado"));
    }
}