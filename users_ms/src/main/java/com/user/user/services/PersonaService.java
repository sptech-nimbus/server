package com.user.user.services;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.user.user.domains.persona.ChatUserDTO;
import com.user.user.domains.persona.Persona;
import com.user.user.domains.responseMessage.ResponseMessage;
import com.user.user.repositories.AthleteRepository;
import com.user.user.repositories.CoachRepository;

@SuppressWarnings("rawtypes")
@Service
public class PersonaService {

    private final AthleteRepository athleteRepo;

    private final CoachRepository coachRepo;

    public PersonaService(AthleteRepository athleteRepo, CoachRepository coachRepo) {
        this.athleteRepo = athleteRepo;
        this.coachRepo = coachRepo;
    }

    public ResponseEntity<ResponseMessage> getPersonaByUserId(UUID id) {
        Persona personaFound = athleteRepo.findAthleteByUserId(id);

        if (personaFound == null) {
            personaFound = coachRepo.findCoachByUserId(id).get();
        }

        if (personaFound == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseMessage<>("Informações de usuário não encontradas"));
        }

        return ResponseEntity.ok(new ResponseMessage<Persona>(personaFound));
    }

    public ResponseEntity<ResponseMessage> getChatUserByUserId(UUID id) {
        Persona personaFound = athleteRepo.findAthleteByUserId(id);

        if (personaFound == null) {
            personaFound = coachRepo.findCoachByUserId(id).get();
        }

        if (personaFound == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseMessage<>("Informações de usuário não encontradas"));
        }

        ChatUserDTO chatUser = new ChatUserDTO(personaFound);

        return ResponseEntity.ok(new ResponseMessage<ChatUserDTO>(chatUser));
    }

    public Boolean checkPersonaCredencials(Persona persona) {
        return checkFieldsNotNull(persona)
                && checkValidPhone(persona.getPhone())
                && checkPlus18(persona.getBirthDate());
    }

    public Boolean checkFieldsNotNull(Persona persona) {
        return persona.getBirthDate() != null &&
                persona.getFirstName() != null &&
                persona.getLastName() != null &&
                persona.getUser() != null;
    }

    Integer[] validsDDD = {
            11, 12, 13, 14, 15, 16, 17, 18, 19,
            21, 22, 24, 27, 28, 31, 32, 33, 34,
            35, 37, 38, 41, 42, 43, 44, 45, 46,
            47, 48, 49, 51, 53, 54, 55, 61, 62,
            64, 63, 65, 66, 67, 68, 69, 71, 73,
            74, 75, 77, 79, 81, 82, 83, 84, 85,
            86, 87, 88, 89, 91, 92, 93, 94, 95,
            96, 97, 98, 99
    };

    public Boolean checkValidPhone(String phone) {
        if (phone == null)
            return true;

        phone = phone.replaceAll("[^0-9]", "");

        return java.util.Arrays.asList(validsDDD).indexOf(Integer.parseInt(phone.substring(0, 2))) > -1
                && phone.length() == 11;
    }

    public Boolean checkPlus18(LocalDate birthDate) {
        return birthDate.plusYears(18).isBefore(LocalDate.now());
    }
}