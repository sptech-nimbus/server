package com.user.user.services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.user.user.domains.email.EmailDTO;
import com.user.user.domains.operationCodes.OperationCode;
import com.user.user.domains.persona.Persona;
import com.user.user.domains.responseMessage.ResponseMessage;
import com.user.user.domains.user.ChangePasswordDTO;
import com.user.user.domains.user.ChangePasswordRequestDTO;
import com.user.user.domains.user.User;
import com.user.user.domains.user.UserDTO;
import com.user.user.exceptions.ResourceNotFoundException;
import com.user.user.repositories.UserRepository;
import com.user.user.utils.CodeGenerator;

@SuppressWarnings("rawtypes")
@Service
public class UserService {
    private final UserRepository repo;
    private final CoachService coachService;
    private final AthleteService athleteService;
    private final EmailService emailService;
    private final OperationCodeService operationCodeService;

    public UserService(UserRepository repo, CoachService coachService, AthleteService athleteService,
            EmailService emailService, OperationCodeService operationCodeService) {
        this.repo = repo;
        this.coachService = coachService;
        this.athleteService = athleteService;
        this.emailService = emailService;
        this.operationCodeService = operationCodeService;
    }

    public ResponseEntity<ResponseMessage> register(UserDTO dto) {
        List<String> credencialsErrors = checkAllUserCredencials(dto);

        if (!credencialsErrors.isEmpty()) {
            return ResponseEntity.status(400)
                    .body(new ResponseMessage("Verifique suas credenciais de usuário.",
                            String.join(", ", credencialsErrors)));
        }

        if (repo.findByEmail(dto.email()).isPresent()) {
            return ResponseEntity.status(409).body(new ResponseMessage("Email já utilizado."));
        }

        User newUser = new User();

        BeanUtils.copyProperties(dto, newUser);

        repo.save(newUser);

        if (dto.coach() != null) {
            ResponseEntity<ResponseMessage> coachResponseEntity = coachService.register(dto.coach(), newUser);

            if (!coachResponseEntity.getStatusCode().equals(HttpStatusCode.valueOf(200))) {
                repo.delete(newUser);

                return coachResponseEntity;
            }
        } else if (dto.athlete() != null) {
            ResponseEntity<ResponseMessage> athleteResponseEntity = athleteService.register(dto.athlete(), newUser);

            if (!athleteResponseEntity.getStatusCode().equals(HttpStatusCode.valueOf(200))) {
                repo.delete(newUser);

                return athleteResponseEntity;
            }
        } else {
            return ResponseEntity.status(400)
                    .body(new ResponseMessage("Tipo de usuário não permitido ou não informado."));
        }

        return ResponseEntity
                .status(200).body(new ResponseMessage<UUID>("Cadastro realizado", newUser.getId()));
    }

    public ResponseEntity<ResponseMessage> login(UserDTO dto) {
        User userFound = repo.findByEmailAndPassword(dto.email(), dto.password())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário", null));

        String userType = userFound.getAthlete() != null ? "Athlete" : "Coach";

        return ResponseEntity.status(200)
                .body(new ResponseMessage<UUID>("Login realizado.", userType, userFound.getId()));
    }

    public ResponseEntity<ResponseMessage> changePasswordRequest(ChangePasswordRequestDTO dto) {
        User userFound = repo.findById(dto.id()).orElseThrow(() -> new ResourceNotFoundException("Usuário", dto.id()));

        Persona personaFound = userFound.getAthlete() == null ? userFound.getCoach() : userFound.getAthlete();

        String recuperationCode = CodeGenerator.codeGen(6, true);

        try {
            operationCodeService.insertCode(
                    new OperationCode("change-password", recuperationCode, dto.expirationDate().plusHours(2), userFound,
                            null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseMessage("Erro ao cadastrar código", e.getMessage()));
        }

        try {

            String emailContent = "<style>\r\n" + //
                    "@import url('https://fonts.cdnfonts.com/css/catamaran');\r\n" + //
                    "</style>" 
                    + "<div style=\"display: flex; justify-content: center; background-color: #c9c9c9; font-family: Cambria, Cochin, Georgia, Times, 'Times New Roman', serif;\">"
                    + "<div style=\"background-color: #131313; width: 40%; color: #FFEAE0\">"
                    + "<div style=\"margin-left: 95px; margin-top: 15px;\">"
                    + "<img style=\"width: 150px; height: 80px;\" src=\"https://raw.githubusercontent.com/sptech-nimbus/server/dev/users_ms/src/main/java/com/user/user/utils/img/logo-email.png\" alt=\"\">"
                    + "</div>"
                    + "<div style=\"border-top: 2px solid #FF7425; margin-top: 15px;\"></div>"
                    + "<div style=\"margin-left: 20%; width: 80%;\">"
                    + "<h3 style=\"font-size: 20px; font-family: 'Catamaran', sans-serif;\" >Olá, <b>" + personaFound.getFirstName() + " "
                    + personaFound.getLastName() + "!</b></h3><br>"
                    + "</div>"
                    + "<div style=\"margin-left: 5%; width: 90%;\">"
                    + "<h4 style=\"font-size: 18px; font-family: 'Catamaran', sans-serif;\">Reconhecemos sua tentativa de mudança de senha. Caso realmente seja o caso, utilize o código abaixo para fazer a mudança de sua senha.</h4><br>"
                    + "</div>"
                    + "<div style=\"display: flex; justify-content: center; align-items: center; margin-left: 5%; width: 300px; height: 100px; border: 3px solid #FF7425 ;\">"
                    + "<h2>" + recuperationCode + "</h2>"
                    + "</div>"
                    + "<img style=\"margin-top: 30px; width: 318px; height: 150px; margin-left: 17px\" src=\"https://raw.githubusercontent.com/sptech-nimbus/server/dev/users_ms/src/main/java/com/user/user/utils/img/footer-email.png\" alt=\"\">"
                    + "</div>"
                    + "</div>";

            emailService
                    .sendHtmlEmail(new EmailDTO(userFound.getEmail(), "Código de recuperação de senha", emailContent));

        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseMessage("Erro ao mandar email", e.getMessage()));
        }

        return ResponseEntity.status(200)
                .body(new ResponseMessage("Verifique seu email com o código de verificação para recuperação de senha"));
    }

    public ResponseEntity<ResponseMessage> changePassword(UUID id, ChangePasswordDTO dto) {
        User userFound = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Usuário", id));

        if (userFound.getPassword().equals(dto.oldPassword())) {
            if (checkUserPassword(dto.newPassword())) {
                userFound.setPassword(dto.newPassword());

                repo.save(userFound);
            } else {
                return ResponseEntity.status(409).body(new ResponseMessage("Senha fora dos padrões."));
            }
        } else {
            return ResponseEntity.status(400).body(new ResponseMessage("Antiga senha incorreta."));
        }

        return ResponseEntity.status(200).body(new ResponseMessage("Senha alterada com sucesso."));
    }

    public ResponseEntity<ResponseMessage> deleteUser(UUID id, String password) {
        User userFound = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Usuário", id));

        if (!String.valueOf(userFound.getPassword()).equals(password)) {
            return ResponseEntity.status(400).body(new ResponseMessage("Senha incorreta."));
        }

        if (userFound.getAthlete() != null) {
            try {
                athleteService.removeUserFromAthlete(userFound.getAthlete().getId());
            } catch (ResourceNotFoundException e) {
                throw new ResourceNotFoundException("Atleta", userFound.getAthlete().getId());
            }
        } else {
            try {
                coachService.removeUserFromCoach(userFound.getCoach().getId());
            } catch (ResourceNotFoundException e) {
                throw new ResourceNotFoundException("Atleta", userFound.getCoach().getId());
            }
        }

        repo.delete(userFound);

        return ResponseEntity.status(200).body(new ResponseMessage("Usuário deletado."));
    }

    public List<String> checkAllUserCredencials(UserDTO dto) {
        List<String> errors = new ArrayList<>();

        if (!checkUserEmail(dto.email())) {
            errors.add("Email inválido");
        }

        if (!checkUserPassword(dto.password())) {
            errors.add("Senha inválida");
        }

        return errors;
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