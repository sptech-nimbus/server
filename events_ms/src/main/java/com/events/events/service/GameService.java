package com.events.events.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.events.events.domain.coach.Coach;
import com.events.events.domain.game.Game;
import com.events.events.domain.game.GameDTO;
import com.events.events.domain.game.GameWithTeams;
import com.events.events.domain.game.GamewResultsDTO;
import com.events.events.domain.responseMessage.ResponseMessage;
import com.events.events.domain.team.Team;
import com.events.events.exception.ResourceNotFoundException;
import com.events.events.repository.GameRepository;

@Service
public class GameService {
    private final GameRepository repo;
    private final RestTemplateService<Team> teamService;
    private final RestTemplateService<Coach> coachService;
    private final SimpMessagingTemplate wsMsgTemplate;

    public GameService(GameRepository repo, RestTemplateService<Team> teamService,
            RestTemplateService<Coach> coachService, SimpMessagingTemplate wsMsgTemplate) {
        this.repo = repo;
        this.teamService = teamService;
        this.coachService = coachService;
        this.wsMsgTemplate = wsMsgTemplate;
    }

    public GamewResultsDTO getLastGame(UUID teamId, LocalDateTime now) {
        List<Game> nextGamesFound = repo.findLastGames(teamId);

        if(nextGamesFound.isEmpty()) {
            throw new ResourceNotFoundException("Jogo", teamId);
        }

        Game gameFound = nextGamesFound.get(0);

        Team challenger = teamService.exchange("3000", "teams/ms-get-team", gameFound.getChallenger(), null,
                Team.class);

        Team challenged = teamService.exchange("3000", "teams/ms-get-team", gameFound.getChallenged(), null,
                Team.class);

        return new GamewResultsDTO(
                gameFound,
                gameFound.getGameResult(),
                challenger,
                challenged);
    }

    public ResponseEntity<ResponseMessage<List<Game>>> register(List<GameDTO> dtos) {
        List<Game> newGames = new ArrayList<>();

        for (GameDTO dto : dtos) {
            Game newGame = new Game(dto);

            BeanUtils.copyProperties(dtos, newGames);

            repo.save(newGame);

            newGames.add(newGame);
        }

        return ResponseEntity.status(200).body(new ResponseMessage<List<Game>>(newGames));
    }

    public ResponseEntity<ResponseMessage<List<GameWithTeams>>> getGamesWithTeamsFromTeamId(UUID teamId) {
        List<Game> games = repo.findGamesByChallengerOrChallenged(teamId, teamId);

        List<GameWithTeams> gamesWithTeams = new ArrayList<>();

        if (!games.isEmpty()) {
            for (Game game : games) {
                try {
                    GameWithTeams gameWithTeams = new GameWithTeams(
                            teamService.exchange("3000", "teams/ms-get-team", game.getChallenger(), null, Team.class),
                            teamService.exchange("3000", "teams/ms-get-team", game.getChallenged(), null, Team.class),
                            game);

                    gamesWithTeams.add(gameWithTeams);
                } catch (ResourceNotFoundException e) {
                    return ResponseEntity.status(404)
                            .body(new ResponseMessage<List<GameWithTeams>>(e.getMessage()));
                } catch (Exception e) {
                    return ResponseEntity.status(500)
                            .body(new ResponseMessage<List<GameWithTeams>>("Serviço de usuários fora do ar no momento",
                                    e.getMessage()));
                }
            }

            return ResponseEntity.status(200).body(new ResponseMessage<List<GameWithTeams>>(gamesWithTeams));
        }

        return ResponseEntity.status(204)
                .body(new ResponseMessage<List<GameWithTeams>>("Nenhum jogo encontrado"));
    }

    public List<Game> getGamesFromTeamId(UUID teamId) {
        List<Game> games = repo.findGamesByChallengerOrChallenged(teamId, teamId);

        return games;
    }

    public GameWithTeams getNextGameFromTeamId(UUID teamId, LocalDateTime date) {
        List<Game> nextGames = repo.findNextGames(teamId);

        if (nextGames.isEmpty()) {
            throw new ResourceNotFoundException("Jogo", teamId);
        }

        Game nextGameFound = nextGames.get(0);

        Team challenger = teamService.exchange("3000", "teams/ms-get-team", nextGameFound.getChallenger(), null,
                Team.class);

        Team challenged = teamService.exchange("3000", "teams/ms-get-team", nextGameFound.getChallenged(), null,
                Team.class);

        return new GameWithTeams(challenger, challenged, nextGameFound);
    }

    public ResponseEntity<ResponseMessage<Game>> confirmGame(UUID id, Coach coach) {
        Game game = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Jogo", id));
        Coach coachFound;

        try {
            coachFound = coachService.getTemplateById("3000", "coaches/ms-get-coach", coach.getId(), Coach.class);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(new ResponseMessage<Game>("Serviço de usuários fora do ar no momento.", e.getMessage()));
        }

        if (coachFound.getTeams().stream().anyMatch(t -> t.getId().equals(game.getChallenged()))) {
            if (game.getConfirmed()) {
                return ResponseEntity.status(409).body(new ResponseMessage<Game>("Jogo já confirmado"));
            }

            game.setConfirmed(true);

            repo.save(game);
        } else {
            return ResponseEntity.status(400)
                    .body(new ResponseMessage<Game>("Apenas o time desafiado pode confirmar um jogo"));
        }

        ResponseMessage<Game> responseMessage = new ResponseMessage<Game>(
                "Jogo confirmado para " + game.getInicialDateTime() + "-" + game.getFinalDateTime(), game);

        wsMsgTemplate.convertAndSend("/events/" + game.getChallenged(), responseMessage);
        wsMsgTemplate.convertAndSend("/events/" + game.getChallenger(), responseMessage);

        return ResponseEntity.status(200).body(responseMessage);
    }

    public ResponseEntity<ResponseMessage<Game>> cancelGameById(UUID id, Coach coach) {
        Game game = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Jogo", id));
        Coach coachFound;

        try {
            coachFound = coachService.getTemplateById("3000", "coaches/ms-get-coach", coach.getId(), Coach.class);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ResponseMessage<Game>(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseMessage<Game>(e.getMessage()));
        }

        if (coachFound.getTeams().stream()
                .anyMatch(team -> team.getId().equals(game.getChallenged())
                        || team.getId().equals(game.getChallenger()))) {
            return ResponseEntity.status(409)
                    .body(new ResponseMessage<Game>("Apenas o treinador de um dos times do jogo pode cancela-lo"));
        }

        repo.delete(game);

        return ResponseEntity.status(200).body(new ResponseMessage<Game>("Jogo cancelado"));
    }

    public Game msGetGameById(UUID id) throws ResourceNotFoundException {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Jogo", id));
    }
}