package com.events.events.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.events.events.domains.coach.Coach;
import com.events.events.domains.game.Game;
import com.events.events.domains.game.GameDTO;
import com.events.events.domains.game.GameWithTeams;
import com.events.events.domains.responseMessage.ResponseMessage;
import com.events.events.domains.team.Team;
import com.events.events.exceptions.ResourceNotFoundException;
import com.events.events.repositories.GameRepository;

@SuppressWarnings("rawtypes")
@Service
public class GameService {
    @Autowired
    GameRepository repo;

    @Autowired
    RestTemplateService<Team> teamService;

    @Autowired
    RestTemplateService<Coach> coachService;

    public ResponseEntity<ResponseMessage> register(GameDTO dto) {
        Game newGame = new Game(dto);

        repo.save(newGame);

        return ResponseEntity.ok(new ResponseMessage<Game>(newGame));
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
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new ResponseMessage(e.getMessage()));
                } catch (Exception e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(new ResponseMessage("Serviço de usuários fora do ar no momento", e.getMessage()));
                }
            }

            return ResponseEntity.ok(new ResponseMessage<List<GameWithTeams>>(gamesWithTeams));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseMessage<>("Nenhum jogo encontrado"));
    }

    public ResponseEntity<ResponseMessage> confirmGame(UUID id, Coach coach) {
        Game game;
        Coach coachFound;

        try {
            coachFound = coachService.getTemplateById("3000", "coaches/ms-get-coach", coach.getId(), Coach.class);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseMessage<>(e.getMessage()));
        }

        try {
            game = findGameById(id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage(e.getMessage()));
        }

        if (coachFound.getTeams().stream().anyMatch(t -> t.getId().equals(game.getChallenged()))) {
            if (game.getConfirmed()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseMessage<>("Jogo já confirmado"));
            }

            game.setConfirmed(true);

            repo.save(game);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseMessage("Apenas o time desafiado pode confirmar um jogo"));
        }

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Jogo confirmado"));
    }

    public ResponseEntity<ResponseMessage> cancelGameById(UUID id, Team team) {
        Game game;

        try {
            game = findGameById(id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage(e.getMessage()));
        }

        if (team.getId().equals(game.getChallenged()) || team.getId().equals(game.getChallenger()))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ResponseMessage<>("Apenas times do jogo podem cancelar o jogo"));

        repo.delete(game);

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Jogo cancelado"));
    }

    public Game findGameById(UUID id) throws Exception {
        Optional<Game> game = repo.findById(id);

        if (!game.isPresent()) {
            throw new Exception("Jogo não encontrado");
        }

        return game.get();
    }
}