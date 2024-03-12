package com.user.user.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.user.user.domains.athleteDesc.AthleteDesc;
import com.user.user.domains.athleteDesc.AthleteDescDTO;
import com.user.user.domains.responseMessage.ResponseMessage;
import com.user.user.repositories.AthleteDescRepository;
import com.user.user.repositories.AthleteRepository;

@SuppressWarnings("rawtypes")
@Service
public class AthleteDescService {
    @Autowired
    AthleteDescRepository repo;

    @Autowired
    AthleteRepository athleteRepo;

    public ResponseEntity<ResponseMessage> register(AthleteDescDTO dto) {
        AthleteDesc newAthleteDesc = new AthleteDesc(dto);

        repo.save(newAthleteDesc);

        return ResponseEntity.ok(new ResponseMessage<AthleteDesc>(newAthleteDesc));
    }

    public ResponseEntity<ResponseMessage> getAthleteDescsByAthleteId(UUID athleteId) {
        Optional<AthleteDesc> athleteDesc = repo.findByAthleteId(athleteId);

        if (!athleteDesc.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseMessage<>("Informações de atleta não encontradas"));
        }

        return ResponseEntity.ok(new ResponseMessage<AthleteDesc>(athleteDesc.get()));
    }

    public ResponseEntity<ResponseMessage> putAthleteDescByAthleteId(UUID athleteId, AthleteDescDTO dto) {
        Optional<AthleteDesc> athleteDesc = repo.findByAthleteId(athleteId);

        if (!athleteDesc.isPresent()) {
            return ResponseEntity.status(204).body(new ResponseMessage("Informações de atleta não encontradas"));
        }

        athleteDesc.get().setHeight(dto.height());
        athleteDesc.get().setWeight(dto.weight());
        athleteDesc.get().setPosition(dto.position());

        repo.save(athleteDesc.get());

        return ResponseEntity.ok().body(new ResponseMessage("Informações de atleta atualizadas"));
    }
}