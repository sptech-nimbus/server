package com.user.user.services;

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
        System.out.println(dto);
        AthleteDesc newAthleteDesc = new AthleteDesc(dto);

        if (!checkAthlete(newAthleteDesc.getAthlete().getId())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage<>("Atleta não encontrado"));
        }

        repo.save(newAthleteDesc);

        return ResponseEntity.ok(new ResponseMessage<AthleteDesc>(newAthleteDesc));
    }

    public ResponseEntity<ResponseMessage> getAthleteDescsByAthleteId(String athleteId) {
        AthleteDesc athleteDesc = repo.findByAthleteId(athleteId);

        if (athleteDesc == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseMessage<>("Informações de atleta não encontradas"));
        }

        return ResponseEntity.ok(new ResponseMessage<AthleteDesc>(athleteDesc));
    }

    public Boolean checkAthlete(String athleteId) {
        return !athleteRepo.findById(athleteId).isEmpty();
    }
}