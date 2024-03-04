package com.user.user.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.user.user.models.athlete.Athlete;
import com.user.user.models.athlete.AthleteDTO;
import com.user.user.models.responseMessage.ResponseMessage;
import com.user.user.models.user.User;
import com.user.user.repositories.AthleteRepository;

@SuppressWarnings("rawtypes")
@Service
public class AthleteService {
    @Autowired
    private AthleteRepository repo;

    public ResponseEntity<ResponseMessage> register(AthleteDTO dto, User user) {
        Athlete newAthlete = new Athlete(dto);

        newAthlete.setUser(user);

        repo.save(newAthlete);

        return ResponseEntity
                .ok(new ResponseMessage<String>("Cadastro realizado", "Cadastro realizado", newAthlete.getId()));
    }
}