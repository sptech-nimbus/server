package com.user.user.services;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.user.user.models.responseMessage.ResponseMessage;
import com.user.user.models.user.User;
import com.user.user.models.user.UserDTO;
import com.user.user.repositories.UserRepository;

@SuppressWarnings("rawtypes")
@Service
public class UserService {
    @Autowired
    private UserRepository repo;

    @Autowired
    private CoachService coachService;

    @Autowired
    private AthleteService athleteService;

    public ResponseEntity<ResponseMessage> register(UserDTO dto) {
        if (!checkAllUserCredencials(dto)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseMessage("Verifique suas credenciais"));
        }

        if (repo.findByEmail(dto.email()).size() > 0) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseMessage("Email já utilizado"));
        }

        User newUser = new User(dto);

        repo.save(newUser);

        if (dto.coach() != null) {
            ResponseEntity<ResponseMessage> coachResponseEntity = coachService.register(dto.coach(), newUser);

            if (!coachResponseEntity.getStatusCode().equals(HttpStatus.OK)) {
                repo.delete(newUser);

                return coachResponseEntity;
            }
        } else if (dto.athlete() != null) {
            ResponseEntity<ResponseMessage> athleteResponseEntity = athleteService.register(dto.athlete(), newUser);

            if (!athleteResponseEntity.getStatusCode().equals(HttpStatus.OK)) {
                System.out.println("certo");
                repo.delete(newUser);

                return athleteResponseEntity;
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseMessage<>("Tipo de usuário não permitido ou não informado"));
        }

        return ResponseEntity
                .ok(new ResponseMessage<String>("Cadastro realizado", "Cadastro realizado", newUser.getId()));
    }

    public ResponseEntity<ResponseMessage> login(UserDTO dto) {
        User userFound = repo.findByEmailAndPassword(dto.email(), dto.password());

        if (userFound == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage("Email ou senha incorretos"));
        }

        return ResponseEntity
                .ok(new ResponseMessage<String>("Login realizado", "Login realizado", userFound.getId()));
    }

    public Boolean checkAllUserCredencials(UserDTO newUser) {
        return checkUserEmail(newUser.email())
                && checkUserPassword(newUser.password());
    }

    public Boolean checkUserEmail(String email) {
        return Pattern.compile("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")
                .matcher(email)
                .matches();
    }

    public Boolean checkUserPassword(String password) {
        return Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$")
                .matcher(password).matches();
    }
}