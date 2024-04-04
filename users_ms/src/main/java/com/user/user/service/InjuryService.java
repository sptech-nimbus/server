package com.user.user.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.user.user.domain.athlete.Athlete;
import com.user.user.domain.injury.Injury;
import com.user.user.domain.injury.InjuryDTO;
import com.user.user.domain.responseMessage.ResponseMessage;
import com.user.user.exception.ResourceNotFoundException;
import com.user.user.repository.AthleteRepository;
import com.user.user.repository.InjuryRepository;

@Service
public class InjuryService {
    private final InjuryRepository repo;
    private final AthleteRepository athleteRepo;

    public InjuryService(InjuryRepository repo, AthleteRepository athleteRepo) {
        this.repo = repo;
        this.athleteRepo = athleteRepo;
    }

    public ResponseEntity<ResponseMessage<Injury>> register(InjuryDTO dto) {
        Injury newInjury = new Injury();

        BeanUtils.copyProperties(dto, newInjury);

        try {
            repo.save(newInjury);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseMessage<>(e.getMessage()));
        }

        return ResponseEntity.status(200).body(new ResponseMessage<Injury>(newInjury));
    }

    public ResponseEntity<ResponseMessage<List<Injury>>> getByAthleteId(UUID athleteId) {
        Athlete athleteFound = athleteRepo.findById(athleteId)
                .orElseThrow(() -> new ResourceNotFoundException("Atleta", athleteId));

        List<Injury> injuriesFound = repo.findAllByAthlete(athleteFound);

        if (injuriesFound.isEmpty())
            return ResponseEntity.status(204).body(new ResponseMessage<>("Atleta sem registros de lesões"));

        return ResponseEntity.status(200).body(new ResponseMessage<List<Injury>>(injuriesFound));
    }

    public ResponseEntity<ResponseMessage<?>> putInjury(UUID id, InjuryDTO dto) {
        Injury injuryFound = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Lesão", id));

        BeanUtils.copyProperties(dto, injuryFound);

        repo.save(injuryFound);

        return ResponseEntity.status(200).body(new ResponseMessage<>("Lesão atualizada"));
    }

    public ResponseEntity<ResponseMessage<?>> deleteInjury(UUID id) {
        Injury injuryFound = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Lesão", id));

        try {
            repo.delete(injuryFound);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(new ResponseMessage<>("Erro ao deletar registro de lesão", e.getMessage()));
        }
        return ResponseEntity.status(200)
                .body(new ResponseMessage<>("Registro de lesão deletado"));
    }
}
