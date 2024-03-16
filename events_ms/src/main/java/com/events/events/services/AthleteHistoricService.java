package com.events.events.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.events.events.domains.athlete.Athlete;
import com.events.events.domains.athleteHistoric.AthleteHistoric;
import com.events.events.domains.athleteHistoric.AthleteHistoricDTO;
import com.events.events.domains.responseMessage.ResponseMessage;
import com.events.events.exceptions.ResourceNotFoundException;
import com.events.events.repositories.AthleteHistoricRepository;

@SuppressWarnings("rawtypes")
@Service
public class AthleteHistoricService {
    @Autowired
    AthleteHistoricRepository repo;

    @Autowired
    RestTemplateService<Athlete> athleteService;

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
}