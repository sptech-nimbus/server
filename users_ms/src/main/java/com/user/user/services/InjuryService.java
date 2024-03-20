package com.user.user.services;

import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.user.user.domains.injury.Injury;
import com.user.user.domains.injury.InjuryDTO;
import com.user.user.domains.responseMessage.ResponseMessage;
import com.user.user.exceptions.ResourceNotFoundException;
import com.user.user.repositories.InjuryRepository;

@SuppressWarnings("rawtypes")
@Service
public class InjuryService {
    @Autowired
    InjuryRepository repo;

    public ResponseEntity<ResponseMessage> register(InjuryDTO dto) {
        Injury newInjury = new Injury();

        BeanUtils.copyProperties(dto, newInjury);

        try {
            repo.save(newInjury);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseMessage(e.getMessage()));
        }

        return ResponseEntity.status(200).body(new ResponseMessage<Injury>(newInjury));
    }

    public ResponseEntity<ResponseMessage> putInjury(UUID id, InjuryDTO dto) {
        Injury injuryFound = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Lesão", id));

        BeanUtils.copyProperties(dto, injuryFound);

        repo.save(injuryFound);

        return ResponseEntity.status(200).body(new ResponseMessage("Lesão atualizada"));
    }

    public ResponseEntity<ResponseMessage> deleteInjury(UUID id) {
        Injury injuryFound = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Lesão", id));

        try {
            repo.delete(injuryFound);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(new ResponseMessage("Erro ao deletar registro de lesão", e.getMessage()));
        }
        return ResponseEntity.status(200)
                .body(new ResponseMessage("Registro de lesão deletado"));
    }
}
