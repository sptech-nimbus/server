package com.user.user.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.user.user.domains.athlete.Athlete;
import com.user.user.domains.athlete.AthleteDTO;
import com.user.user.domains.responseMessage.ResponseMessage;
import com.user.user.domains.user.User;
import com.user.user.repositories.AthleteRepository;

@SuppressWarnings("rawtypes")
@Service
public class AthleteService extends PersonaService implements _persona<AthleteDTO> {
    @Autowired
    private AthleteRepository repo;

    public ResponseEntity<ResponseMessage> register(AthleteDTO dto, User user) {
        Athlete newAthlete = new Athlete(dto);
        newAthlete.setUser(user);

        if (!checkPersonaCredencials(newAthlete)
                || !checkCategory(newAthlete.getCategory())
                || !checkIsStarting(newAthlete.getIsStarting())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseMessage<>("Verifique suas credenciais de atleta"));
        }

        repo.save(newAthlete);

        return ResponseEntity
                .ok(new ResponseMessage<UUID>("Cadastro realizado", "Cadastro realizado", newAthlete.getId()));
    }

    public ResponseEntity<ResponseMessage> removeUserFromAthlete(UUID id) {
        Optional<Athlete> athleteFound = repo.findById(id);

        if (!athleteFound.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage<>("Atleta não encontrado."));
        }

        athleteFound.get().setUser(null);

        repo.save(athleteFound.get());

        return ResponseEntity.ok(new ResponseMessage<>("Usuário desvinculado de atleta"));
    }

    @Override
    public ResponseEntity<ResponseMessage> putPersona(UUID id, AthleteDTO dto) {
        Optional<Athlete> athleteFound = repo.findById(id);

        if (!athleteFound.isPresent()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ResponseMessage<>("Atleta não encontrado."));
        }

        BeanUtils.copyProperties(dto, athleteFound.get());

        try {
            repo.save(athleteFound.get());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseMessage<>("Erro ao atualizar atleta.", e.getMessage()));
        }

        return ResponseEntity.ok(new ResponseMessage<>("Atleta atualizado com sucesso"));
    }

    public Boolean checkCategory(String category) {
        return category != null;
    }

    public Boolean checkIsStarting(Boolean isStarting) {
        return isStarting != null;
    }
}