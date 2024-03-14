package com.events.events.services;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.events.events.domains.athleteHistoric.AthleteHistoric;
import com.events.events.domains.athleteHistoric.AthleteHistoricDTO;
import com.events.events.domains.responseMessage.ResponseMessage;
import com.events.events.repositories.AthleteHistoricRepository;

@SuppressWarnings("rawtypes")
@Service
public class AthleteHistoricService {
    @Autowired
    AthleteHistoricRepository repo;

    public ResponseEntity<ResponseMessage> register(AthleteHistoricDTO dto) {
        AthleteHistoric newAthleteHistoric = new AthleteHistoric();

        BeanUtils.copyProperties(dto, newAthleteHistoric);

        try {
            repo.save(newAthleteHistoric);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseMessage<>("Erro ao registrar historico de atleta", e.getMessage()));
        }

        return ResponseEntity.status(201).body(new ResponseMessage<AthleteHistoric>(newAthleteHistoric));
    }
}