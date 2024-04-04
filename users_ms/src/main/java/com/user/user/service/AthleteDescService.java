package com.user.user.service;

import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.user.user.domain.athleteDesc.AthleteDesc;
import com.user.user.domain.athleteDesc.AthleteDescDTO;
import com.user.user.domain.responseMessage.ResponseMessage;
import com.user.user.exception.ResourceNotFoundException;
import com.user.user.repository.AthleteDescRepository;
import com.user.user.repository.AthleteRepository;

@Service
public class AthleteDescService {
    private final AthleteDescRepository repo;

    private final AthleteRepository athleteRepo;

    public AthleteDescService(AthleteDescRepository repo, AthleteRepository athleteRepo) {
        this.repo = repo;
        this.athleteRepo = athleteRepo;
    }

    public ResponseEntity<ResponseMessage<AthleteDesc>> register(AthleteDescDTO dto) {
        athleteRepo.findById(dto.athlete().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Atleta", dto.athlete().getId()));

        AthleteDesc newAthleteDesc = new AthleteDesc();

        BeanUtils.copyProperties(dto, newAthleteDesc);

        repo.save(newAthleteDesc);

        return ResponseEntity.status(200).body(new ResponseMessage<AthleteDesc>(newAthleteDesc));
    }

    public ResponseEntity<ResponseMessage<AthleteDesc>> getAthleteDescsByAthleteId(UUID athleteId) {
        AthleteDesc athleteDesc = repo.findByAthleteId(athleteId)
                .orElseThrow(() -> new ResourceNotFoundException("Informações de atleta", athleteId));

        return ResponseEntity.status(200).body(new ResponseMessage<AthleteDesc>(athleteDesc));
    }

    public ResponseEntity<ResponseMessage<?>> putAthleteDescByAthleteId(UUID athleteId, AthleteDescDTO dto) {
        AthleteDesc athleteDesc = repo.findByAthleteId(athleteId)
                .orElseThrow(() -> new ResourceNotFoundException("Informações de atleta", athleteId));

        BeanUtils.copyProperties(dto, athleteDesc);

        repo.save(athleteDesc);

        return ResponseEntity.status(200).body(new ResponseMessage<>("Informações de atleta atualizadas"));
    }
}