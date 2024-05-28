package com.user.user.service;

import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.user.user.domain.athleteDesc.AthleteDesc;
import com.user.user.domain.athleteDesc.AthleteDescDTO;
import com.user.user.domain.athleteDesc.AthleteDescwAthleteDTO;
import com.user.user.domain.athleteHistoric.AthleteHistoric;
import com.user.user.domain.responseMessage.ResponseMessage;
import com.user.user.exception.ResourceNotFoundException;
import com.user.user.repository.AthleteDescRepository;
import com.user.user.repository.AthleteRepository;

@Service
public class AthleteDescService {
    private final AthleteDescRepository repo;

    private final AthleteRepository athleteRepo;

    private final AthleteHistoricService athleteHistoricService;

    public AthleteDescService(AthleteDescRepository repo, AthleteRepository athleteRepo,
            AthleteHistoricService athleteHistoricService) {
        this.repo = repo;
        this.athleteRepo = athleteRepo;
        this.athleteHistoricService = athleteHistoricService;
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

    public AthleteDescwAthleteDTO getAthleteAllInfo(UUID athleteId) {
        AthleteDesc athleteDesc = repo.findByAthleteId(athleteId)
                .orElseThrow(() -> new ResourceNotFoundException("Informações de atleta", athleteId));

        Page<AthleteHistoric> historicPages = athleteHistoricService.getAthleteHistoricsPageByAthleteId(athleteId, 1,
                5);

        Integer points = 0, assists = 0, rebounds = 0;

        for (AthleteHistoric ah : historicPages) {
            points += ah.getTwoPointsConverted() + ah.getThreePointsConverted();
            assists += ah.getAssists();
            rebounds += ah.getDefRebounds() + ah.getOffRebounds();
        }

        return new AthleteDescwAthleteDTO(athleteDesc, points, assists, rebounds);
    }

    public void deleteAthleteDescById(UUID athleteId) {
        AthleteDesc athleteDesc = repo.findByAthleteId(athleteId)
                .orElseThrow(() -> new ResourceNotFoundException("Descrição de atleta", athleteId));

        repo.delete(athleteDesc);
    }
}