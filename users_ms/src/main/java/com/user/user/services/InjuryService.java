package com.user.user.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.user.user.domains.injury.Injury;
import com.user.user.domains.injury.InjuryDTO;
import com.user.user.domains.responseMessage.ResponseMessage;
import com.user.user.repositories.InjuryRepository;

@SuppressWarnings("rawtypes")
@Service
public class InjuryService {
    @Autowired
    InjuryRepository repo;

    public ResponseEntity<ResponseMessage> register(InjuryDTO dto) {
        Injury newInjury = new Injury(dto);

        repo.save(newInjury);

        return ResponseEntity.ok(new ResponseMessage<Injury>(newInjury));
    }
}
