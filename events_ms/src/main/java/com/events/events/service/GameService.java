package com.events.events.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.events.events.domain.coach.Coach;
import com.events.events.domain.game.Game;
import com.events.events.domain.game.GameDTO;
import com.events.events.domain.game.GameWithTeams;
import com.events.events.domain.responseMessage.ResponseMessage;
import com.events.events.domain.team.Team;
import com.events.events.exception.ResourceNotFoundException;
import com.events.events.repository.GameRepository;

@SuppressWarnings("rawtypes")
@Service
public class GameService {
    private final GameRepository repo;
    private final RestTemplateService<Team> teamService;
    private final RestTemplateService<Coach> coachService;

    public GameService(GameRepository repo, RestTemplateService<Team> teamService,
            RestTemplateService<Coach> coachService) {
        this.repo = repo;
        this.teamService = teamService;
        this.coachService = coachService;
    }

    public ResponseEntity<ResponseMessage> register(GameDTO dto) {
        Game newGame = new Game(dto);

        repo.save(newGame);

        return ResponseEntity.status(200).body(new ResponseMessage<Game>(newGame));
    }

    public ResponseEntity<ResponseMessage> getGamesFromTeamId(UUID teamId) {
        List<Game> games = repo.findGamesByChallengerOrChallenged(teamId, teamId);

        List<GameWithTeams> gamesWithTeams = new ArrayList<>();

        if (!games.isEmpty()) {
            for (Game game : games) {
                try {
                    GameWithTeams gameWithTeams = new GameWithTeams(
                            teamService.getTemplateById("3000", "teams/ms-get-team", game.getChallenger(), Team.class),
                            teamService.getTemplateById("3000", "teams/ms-get-team", game.getChallenged(), Team.class),
                            game);

                    gamesWithTeams.add(gameWithTeams);
                } catch (ResourceNotFoundException e) {
                    return ResponseEntity.status(404)
                            .body(new ResponseMessage(e.getMessage()));
                } catch (Exception e) {
                    return ResponseEntity.status(500)
                            .body(new ResponseMessage("Serviço de usuários fora do ar no momento", e.getMessage()));
                }
            }

            return ResponseEntity.status(200).body(new ResponseMessage<List<GameWithTeams>>(gamesWithTeams));
        }

        return ResponseEntity.status(204)
                .body(new ResponseMessage("Nenhum jogo encontrado"));
    }

    public ResponseEntity<ResponseMessage> confirmGame(UUID id, Coach coach) {
        Game game = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Jogo", id));
        Coach coachFound;

        try {
            coachFound = coachService.getTemplateById("3000", "coaches/ms-get-coach", coach.getId(), Coach.class);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ResponseMessage(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(new ResponseMessage(e.getMessage()));
        }

        if (coachFound.getTeams().stream().anyMatch(t -> t.getId().equals(game.getChallenged()))) {
            if (game.getConfirmed()) {
                return ResponseEntity.status(409).body(new ResponseMessage<>("Jogo já confirmado"));
            }

            game.setConfirmed(true);

            repo.save(game);
        } else {
            return ResponseEntity.status(400)
                    .body(new ResponseMessage("Apenas o time desafiado pode confirmar um jogo"));
        }

        return ResponseEntity.status(200).body(new ResponseMessage("Jogo confirmado"));
    }

    public ResponseEntity<ResponseMessage> cancelGameById(UUID id, Coach coach) {
        Game game = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Jogo", id));
        Coach coachFound;

        try {
            coachFound = coachService.getTemplateById("3000", "coaches/ms-get-coach", coach.getId(), Coach.class);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ResponseMessage(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseMessage(e.getMessage()));
        }

        if (coachFound.getTeams().stream()
                .anyMatch(team -> team.getId().equals(game.getChallenged())
                        || team.getId().equals(game.getChallenger()))) {
            return ResponseEntity.status(409)
                    .body(new ResponseMessage("Apenas o treinador de um dos times do jogo pode cancela-lo"));
        }

        repo.delete(game);

        return ResponseEntity.status(200).body(new ResponseMessage("Jogo cancelado"));
    }
}