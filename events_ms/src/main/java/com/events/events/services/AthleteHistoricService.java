package com.events.events.services;

import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.events.events.domains.athlete.Athlete;
import com.events.events.domains.athleteHistoric.AthleteHistoric;
import com.events.events.domains.athleteHistoric.AthleteHistoricDTO;
import com.events.events.domains.responseMessage.ResponseMessage;
import com.events.events.repositories.AthleteHistoricRepository;

@SuppressWarnings("rawtypes")
@Service
public class AthleteHistoricService {
    @Autowired
    AthleteHistoricRepository repo;

    @Autowired
    RestTemplateService<Athlete> athleteService;

    public ResponseEntity<ResponseMessage> register(AthleteHistoricDTO dto) {
        Athlete athlete;

        try {
            athlete = athleteService.getTemplateById("3000", "athletes/ms-get-athlete", dto.athleteId(), Athlete.class);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage<>(e.getMessage()));
        }

        AthleteHistoric newAthleteHistoric = new AthleteHistoric();

        BeanUtils.copyProperties(dto, newAthleteHistoric);

        newAthleteHistoric.setAthleteId(athlete.getId());

        try {
            repo.save(newAthleteHistoric);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseMessage<>("Erro ao registrar historico de atleta", e.getMessage()));
        }

        return ResponseEntity.status(201).body(new ResponseMessage<AthleteHistoric>(newAthleteHistoric));
    }

    public ResponseEntity<ResponseMessage> getAthleteHistoricByAthleteId(UUID athleteId) {
        return ResponseEntity.status(201).body(new ResponseMessage<AthleteHistoric>(repo.findById(athleteId).get()));
    }
}