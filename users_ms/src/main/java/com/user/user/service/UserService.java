package com.user.user.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.user.user.config.security.jwt.GerenciadorTokenJwt;
import com.user.user.domain.email.EmailDTO;
import com.user.user.domain.operationCodes.OperationCode;
import com.user.user.domain.persona.NewUserDTO;
import com.user.user.domain.persona.Persona;
import com.user.user.domain.responseMessage.ResponseMessage;
import com.user.user.domain.user.ChangePasswordDTO;
import com.user.user.domain.user.ChangePasswordRequestDTO;
import com.user.user.domain.user.User;
import com.user.user.domain.user.UserDTO;
import com.user.user.domain.user.authentication.dto.UserTokenDTO;
import com.user.user.exception.ResourceNotFoundException;
import com.user.user.repository.UserRepository;
import com.user.user.util.CodeGenerator;

@Service
public class UserService {
    private final UserRepository repo;
    private final CoachService coachService;
    private final AthleteService athleteService;
    private final EmailService emailService;
    private final OperationCodeService operationCodeService;
    private final PasswordEncoder encoder;
    private final GerenciadorTokenJwt gerenciadorTokenJwt;
    private final AuthenticationManager authenticationManager;

    public UserService(UserRepository repo, CoachService coachService, AthleteService athleteService,
            EmailService emailService, OperationCodeService operationCodeService, PasswordEncoder encoder,
            GerenciadorTokenJwt gerenciadorTokenJwt, AuthenticationManager authenticationManager) {
        this.repo = repo;
        this.coachService = coachService;
        this.athleteService = athleteService;
        this.emailService = emailService;
        this.operationCodeService = operationCodeService;
        this.encoder = encoder;
        this.gerenciadorTokenJwt = gerenciadorTokenJwt;
        this.authenticationManager = authenticationManager;
    }

    public ResponseEntity<ResponseMessage<NewUserDTO>> register(UserDTO dto) {
        List<String> credencialsErrors = checkAllUserCredencials(dto);

        if (!credencialsErrors.isEmpty()) {
            return ResponseEntity.status(400)
                    .body(new ResponseMessage<>("Verifique suas credenciais de usuário.",
                            String.join(", ", credencialsErrors)));
        }

        if (repo.findByEmail(dto.email()).isPresent()) {
            return ResponseEntity.status(409).body(new ResponseMessage<>("Email já utilizado."));
        }

        User newUser = new User();

        BeanUtils.copyProperties(dto, newUser);

        String cryptPassword = encoder.encode(newUser.getPassword());

        newUser.setPassword(cryptPassword);

        repo.save(newUser);

        NewUserDTO newUserDTO;

        if (dto.coach() != null && dto.athlete() != null) {
            return ResponseEntity.status(400).body(new ResponseMessage<>("Informe apenas um tipo de usuário"));
        }

        if (dto.coach() != null) {
            ResponseEntity<ResponseMessage<UUID>> coachResponseEntity = coachService.register(dto.coach(), newUser);

            if (!coachResponseEntity.getStatusCode().equals(HttpStatusCode.valueOf(201))) {
                repo.delete(newUser);

                return ResponseEntity.status(400)
                        .body(new ResponseMessage<>(coachResponseEntity.getBody().getClientMsg()));
            }

            newUserDTO = new NewUserDTO(newUser.getId(), coachResponseEntity.getBody().getData());
        } else if (dto.athlete() != null) {
            ResponseEntity<ResponseMessage<UUID>> athleteResponseEntity = athleteService.register(dto.athlete(),
                    newUser);

            if (!athleteResponseEntity.getStatusCode().equals(HttpStatusCode.valueOf(201))) {
                repo.delete(newUser);

                return ResponseEntity.status(400).body(new ResponseMessage<>(
                        athleteResponseEntity.getBody().getClientMsg()));
            }

            newUserDTO = new NewUserDTO(newUser.getId(), athleteResponseEntity.getBody().getData());
        } else {
            return ResponseEntity.status(400)
                    .body(new ResponseMessage<>("Tipo de usuário não permitido ou não informado."));
        }

        return ResponseEntity.status(200).body(new ResponseMessage<NewUserDTO>(newUserDTO));
    }

    public ResponseEntity<ResponseMessage<UserTokenDTO>> login(UserDTO dto) {
        final UsernamePasswordAuthenticationToken credentials = new UsernamePasswordAuthenticationToken(dto.email(),
                dto.password());

        final Authentication auth = this.authenticationManager.authenticate(credentials);

        User userFound = repo.findByEmail(dto.email())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário", null));

        String userType = userFound.getAthlete() != null ? "Athlete" : "Coach";

        SecurityContextHolder.getContext().setAuthentication(auth);

        final String token = gerenciadorTokenJwt.generateToken(auth);

        return ResponseEntity.status(200)
                .body(new ResponseMessage<UserTokenDTO>("Login realizado.", userType,
                        new UserTokenDTO(userFound.getId(), token)));
    }

    public ResponseEntity<ResponseMessage<?>> changePasswordRequest(ChangePasswordRequestDTO dto) {
        User userFound = repo.findById(dto.id()).orElseThrow(() -> new ResourceNotFoundException("Usuário", dto.id()));

        Persona personaFound = userFound.getAthlete() == null ? userFound.getCoach() : userFound.getAthlete();

        String recuperationCode = CodeGenerator.codeGen(6, true);

        try {
            operationCodeService.insertCode(
                    new OperationCode("change-password", recuperationCode, dto.expirationDate().plusHours(2), userFound,
                            null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseMessage<>("Erro ao cadastrar código", e.getMessage()));
        }

        try {
            String emailContent = "<style>\r\n" + //
                    "@import url('https://fonts.cdnfonts.com/css/catamaran');\r\n" + //
                    "</style>"
                    + "<div style=\"display: flex; justify-content: center; background-color: #c9c9c9; font-family: Cambria, Cochin, Georgia, Times, 'Times New Roman', serif;\">"
                    + "<div style=\"background-color: #131313; width: 40%; color: #FFEAE0\">"
                    + "<div style=\"margin-left: 86px; margin-top: 15px;\">"
                    + "<img style=\"width: 180px; height: 150px;\" src=\"https://raw.githubusercontent.com/sptech-nimbus/server/dev/users_ms/src/main/java/com/user/user/utils/img/logo-email.png\" alt=\"\">"
                    + "</div>"
                    + "<div style=\"border-top: 2px solid #FF7425; margin-top: 15px;\"></div>"
                    + "<div style=\"margin-left: 17%; width: 95%;\">"
                    + "<h3 style=\"font-size: 20px; font-family: 'Catamaran', sans-serif;\" >Olá, <b>"
                    + personaFound.getFirstName() + " "
                    + personaFound.getLastName() + "!</b></h3><br>"
                    + "</div>"
                    + "<div style=\"margin-left: 5%; width: 95%;\">"
                    + "<h4 style=\"font-size: 16px; font-family: 'Catamaran', sans-serif;\">Reconhecemos sua tentativa de mudança de senha. Caso realmente seja o caso, utilize o código abaixo para fazer a mudança de sua senha.</h4><br>"
                    + "</div>"
                    + "<div style=\"display: flex; justify-content: center; align-items: center; margin-left: 5%; width: 300px; height: 100px; border: 3px solid #FF7425 ;\">"
                    + "<h2>" + recuperationCode + "</h2>"
                    + "</div>"
                    + "<img style=\"margin-top: 20px; width: 315px; height: 150px; margin-left: 17px\" src=\"https://raw.githubusercontent.com/sptech-nimbus/server/dev/users_ms/src/main/java/com/user/user/utils/img/footer-email.png\" alt=\"\">"
                    + "</div>"
                    + "</div>";

            emailService
                    .sendHtmlEmail(new EmailDTO(userFound.getEmail(), "Código de recuperação de senha", emailContent));

        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseMessage<>("Erro ao mandar email", e.getMessage()));
        }

        return ResponseEntity.status(200)
                .body(new ResponseMessage<>(
                        "Verifique seu email com o código de verificação para recuperação de senha"));
    }

    public ResponseEntity<ResponseMessage<?>> changePassword(UUID id, ChangePasswordDTO dto) {
        User userFound = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Usuário", id));

        if (userFound.getPassword().equals(dto.oldPassword())) {
            if (checkUserPassword(dto.newPassword())) {
                userFound.setPassword(dto.newPassword());

                repo.save(userFound);
            } else {
                return ResponseEntity.status(409).body(new ResponseMessage<>("Senha fora dos padrões."));
            }
        } else {
            return ResponseEntity.status(400).body(new ResponseMessage<>("Antiga senha incorreta."));
        }

        return ResponseEntity.status(200).body(new ResponseMessage<>("Senha alterada com sucesso."));
    }

    public ResponseEntity<ResponseMessage<?>> deleteUser(UUID id, String password) {
        User userFound = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Usuário", id));

        if (!String.valueOf(userFound.getPassword()).equals(password)) {
            return ResponseEntity.status(400).body(new ResponseMessage<>("Senha incorreta."));
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

        return ResponseEntity.status(200).body(new ResponseMessage<>("Usuário deletado."));
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