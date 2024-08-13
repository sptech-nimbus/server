package com.user.user.service;

import com.user.user.controller.TeamController;
import com.user.user.domain.athlete.Athlete;
import com.user.user.domain.athlete.AthleteDTO;
import com.user.user.domain.coach.Coach;
import com.user.user.domain.coach.CoachDTO;
import com.user.user.domain.responseMessage.ResponseMessage;
import com.user.user.domain.team.Team;
import com.user.user.domain.team.TeamDTO;
import com.user.user.domain.user.User;
import com.user.user.exception.ResourceNotFoundException;
import com.user.user.repository.AthleteDescRepository;
import com.user.user.repository.AthleteRepository;
import com.user.user.repository.CoachRepository;
import com.user.user.repository.OperationCodeRepository;
import com.user.user.repository.TeamRepository;
import org.awaitility.core.CheckedExceptionRethrower;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.validateMockitoUsage;
import static org.mockito.Mockito.when;

class TeamServiceTest {
    @Mock
    User user;
    @Mock
    Coach coach;
    @Mock
    Team team;
    @Mock
    Athlete atlete;
    @Mock
    TeamRepository repository;
    @Mock
    CoachRepository coachRepo;
    @Mock
    AthleteRepository athleteRepo;
    @Mock
    OperationCodeRepository operationCodeRepo;
    @Mock
    AthleteDescRepository athleteDescRepository;
    @InjectMocks
    TeamService service;
    private ResponseEntity<ResponseMessage<?>> teamResponse;
    UUID id = UUID.randomUUID();
    CoachDTO coachdto;
    TeamDTO teamDto;
    AthleteDTO athleteDto;
    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
        service = new TeamService(repository, coachRepo, athleteRepo, operationCodeRepo, athleteDescRepository, null);

        coachdto = new CoachDTO("Cafunga", "Carequinha", LocalDate.now(), null, null);
        user = new User(id, "cafunga@email.com" , "Cafunga@123", coach, null);
        coach = new Coach();
        coach.setId(id);
        coach.setUser(user);

        teamDto = new TeamDTO("sub-20", null, "Rua do Meio", "Dragões da rua do meio", coach);

        BeanUtils.copyProperties(coachdto, coach);

        athleteDto = new AthleteDTO("Luciano" , "Hulk" , LocalDate.now(), null, null, "sub-20" , true);
        BeanUtils.copyProperties(athleteDto, atlete);

        BeanUtils.copyProperties(teamDto, team);
    }
    @Test
    @DisplayName("Teste correto se, ao chamar register() retorna o time criado")
    void cenarioCorretoRegister01() {

        Coach coachOpt = (coach);

        when(coachRepo.findById(coach.getId())).thenReturn(Optional.of(coachOpt));

        ResponseEntity<ResponseMessage<Team>> teamResponse = service.register(teamDto);

        Assertions.assertNotNull(teamResponse.getBody().getData());
    }

    @Test
    @DisplayName("Teste incorreto se, ao chamar register() com o id nulo, retorna mensagem de erro")
    void cenarioIncorretoRegister01() {

        Coach coachOpt = (coach);
        when(coachRepo.findById(coach.getId())).thenReturn(Optional.of(coachOpt));

        UUID id = null;
        teamDto.coach().setId(id);

        ResponseEntity<ResponseMessage<Team>> teamResponse = service.register(teamDto);

        Assertions.assertNotNull(teamResponse.getBody().getClientMsg());
    }

    @Test
    @DisplayName("Teste correto se, ao chamar putTeamById() retorna o time atualizado")
    void cenarioCorretoPutTeam01(){

        team.setId(id);

        when(repository.findById(id)).thenReturn(Optional.of(team));

        ResponseEntity<ResponseMessage<?>> teamResponse = service.putTeamById(id, teamDto);
        Assertions.assertNotNull(teamResponse.getBody().getClientMsg());
    }

    @Test
    @DisplayName("Teste Incorreto se, ao chamar putTeamById(), retorna mensagem de erro")
    void cenarioIncorretoPutTeam01(){

        UUID idTeste = UUID.randomUUID();

        assertThrows(ResourceNotFoundException.class,
                () -> service.putTeamById(idTeste, teamDto)
        );
    }

    @Test
    @DisplayName("Teste correto se, ao chamar deleteTeam(), retorna time excluido com sucesso")
    void CenarioCorretoDeleteTeam01() {
        team = new Team();
        team.setId(UUID.randomUUID());
        team.setName("Test Team");
        team.setCoach(coach);
        team.setAthletes(List.of(atlete));
        BeanUtils.copyProperties(teamDto, team);

        Team teamOpt = team;

        Athlete athleteOpt = (atlete);
        team.setAthletes(List.of(atlete));

        when(repository.findById(team.getId())).thenReturn(Optional.of(teamOpt));
        when(athleteRepo.findByTeamId(team.getId())).thenReturn(List.of(athleteOpt));

        ResponseEntity<ResponseMessage<?>> teamResponse = service.deleteTeam(team.getId(), team.getCoach().getUser().getPassword());

        assertNotNull(teamResponse.getBody());
    }

    @Test
    @DisplayName("Teste incorreto se, ao chamar deleteTeam(), retorna senha incorreta")
    void CenarioIncorretoDeleteTeam01() {team = new Team();
        team.setId(UUID.randomUUID());
        team.setName("Test Team");
        team.setCoach(coach);
        team.setAthletes(List.of(atlete));
        BeanUtils.copyProperties(teamDto, team);

        Team teamOpt = team;

        Athlete athleteOpt = (atlete);
        team.setAthletes(List.of(atlete));

        when(repository.findById(team.getId())).thenReturn(Optional.of(teamOpt));
        when(athleteRepo.findByTeamId(team.getId())).thenReturn(List.of(athleteOpt));

        String senha = "123@Cafunga";

        ResponseEntity<ResponseMessage<?>> teamResponse = service.deleteTeam(team.getId(), senha);

        assertNotNull(teamResponse.getBody());
    }
}