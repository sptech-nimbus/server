package com.events.events.services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.events.events.domains.game.Game;
import com.events.events.domains.game.GameDTO;
import com.events.events.domains.game.GameWithTeams;
import com.events.events.domains.responseMessage.ResponseMessage;
import com.events.events.repositories.GameRepository;

@SuppressWarnings("rawtypes")
@Service
public class GameService {
    @Autowired
    GameRepository repo;

    @Autowired
    TeamService teamService;

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
                    GameWithTeams gameWithTeams = new GameWithTeams(teamService.getTeamInfoById(game.getChallenger()),
                    teamService.getTeamInfoById(game.getChallenged()),
                    game);

                    gamesWithTeams.add(gameWithTeams);
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
}
