package com.user.user.services;

import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.user.user.domains.responseMessage.ResponseMessage;
import com.user.user.domains.user.ChangePasswordDTO;
import com.user.user.domains.user.User;
import com.user.user.domains.user.UserDTO;
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
                    .body(new ResponseMessage("Verifique suas credenciais de usuário."));
        }

        if (repo.findByEmail(dto.email()).size() > 0) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseMessage("Email já utilizado."));
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
                repo.delete(newUser);

                return athleteResponseEntity;
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseMessage<>("Tipo de usuário não permitido ou não informado."));
        }

        return ResponseEntity
                .ok(new ResponseMessage<UUID>("Cadastro realizado.", "Cadastro realizado.", newUser.getId()));
    }

    public ResponseEntity<ResponseMessage> login(UserDTO dto) {
        User userFound = repo.findByEmailAndPassword(dto.email(), dto.password());

        if (userFound == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage("Email ou senha incorretos."));
        }

        return ResponseEntity
                .ok(new ResponseMessage<UUID>("Login realizado.", "Login realizado.", userFound.getId()));
    }

    public ResponseEntity<ResponseMessage> changePassword(UUID id, ChangePasswordDTO dto) {
        Optional<User> userFound = repo.findById(id);

        if (!userFound.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage<>("Usuário nâo encontrado"));
        }

        if (userFound.get().getPassword().equals(dto.oldPassword())) {
            userFound.get().setPassword(dto.newPassword());

            repo.save(userFound.get());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage<>("Senha antiga incorreta."));
        }

        return ResponseEntity.ok(new ResponseMessage<>("Senha alterada com sucesso."));
    }

    public ResponseEntity<ResponseMessage> deleteUser(UUID id, String password) {
        Optional<User> userFound = repo.findById(id);

        if (!userFound.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage<>("Usuário não encontrado."));
        }

        if (!userFound.get().getPassword().equals(password)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage<>("Senha incorreta."));
        }

        if (userFound.get().getAthlete() != null) {
            athleteService.removeUserFromAthlete(userFound.get().getAthlete().getId());
        } else if (userFound.get().getCoach() != null) {
            coachService.removeUserFromCoach(userFound.get().getCoach().getId());
        }

        repo.delete(userFound.get());

        return ResponseEntity.ok(new ResponseMessage<>("Usuário deletado."));
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