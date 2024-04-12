package com.events.events.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.events.events.domain.athleteHistoric.AthleteHistoric;
import com.events.events.domain.game.Game;
import com.events.events.domain.gameResult.GameResult;
import com.events.events.domain.graphs.PointsDivisionDTO;
import com.events.events.domain.graphs.ThreePointsConvertedDTO;
import com.events.events.domain.graphs.WinsFromTeamDTO;
import com.events.events.domain.responseMessage.ResponseMessage;
import com.events.events.repository.GameRepository;
import com.events.events.repository.GameResultRepository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Service
public class GraphService {
    private final GameResultRepository gameResultRepo;
    private final GameRepository gameRepo;
    private final RestTemplateService<AthleteHistoricList> athleteHistoricListService;

    public GraphService(GameResultRepository gameResultRepo, GameRepository gameRepo,
            RestTemplateService<AthleteHistoricList> athleteHistoricListService) {
        this.gameResultRepo = gameResultRepo;
        this.gameRepo = gameRepo;
        this.athleteHistoricListService = athleteHistoricListService;
    }

    public ResponseEntity<ResponseMessage<WinsFromTeamDTO>> getWinsByTeam(UUID teamId, Integer matches) {
        List<GameResult> gameResultsFound = gameResultRepo.findGameResultsByTeamWithLimit(teamId, matches);

        if (gameResultsFound.isEmpty())
            return ResponseEntity.status(204)
                    .body(new ResponseMessage<WinsFromTeamDTO>("Sem resultados de jogos encontrados"));

        Integer teamWins = 0;

        for (GameResult gameResult : gameResultsFound) {
            if (gameResult.getChallengedPoints() > gameResult.getChallengerPoints()
                    && gameResult.getGame().getChallenged().equals(teamId)
                    ||
                    gameResult.getChallengerPoints() > gameResult.getChallengedPoints()
                            && gameResult.getGame().getChallenger().equals(teamId)) {
                teamWins++;
            }
        }

        WinsFromTeamDTO winsFromTeamDTO = new WinsFromTeamDTO(teamWins, gameResultsFound.size() - teamWins);

        return ResponseEntity.status(200).body(new ResponseMessage<WinsFromTeamDTO>(winsFromTeamDTO));
    }

    public ResponseEntity<ResponseMessage<List<ThreePointsConvertedDTO>>> getThreePointsConverteAndAttempeddByTeam(
            UUID teamId,
            Integer matches) {
        List<Game> gamesFound = gameRepo.findGamesByChallengerChallengedWithTop(teamId, matches);

        if (gamesFound.isEmpty())
            return ResponseEntity.status(204)
                    .body(new ResponseMessage<>("Time ainda não jogou nenhum jogo"));

        if (gamesFound.stream().allMatch(game -> game.getAthletesHistorics() == null))
            return ResponseEntity.status(204)
                    .body(new ResponseMessage<>("Nenhum histórico de atleta registrado"));

        List<ThreePointsConvertedDTO> threePointsConverted = new ArrayList<>();

        for (Game game : gamesFound) {
            Integer threePointsConvertedThatGame = 0, threePointsAttempedThatGame = 0;

            for (AthleteHistoric athleteHistoric : game.getAthletesHistorics()) {
                threePointsConvertedThatGame += athleteHistoric.getThreePointsConverted();
                threePointsAttempedThatGame += athleteHistoric.getThreePointsAttemped();
            }

            threePointsConverted.add(new ThreePointsConvertedDTO(
                    threePointsAttempedThatGame,
                    threePointsConvertedThatGame,
                    game));
        }

        return ResponseEntity.status(200)
                .body(new ResponseMessage<List<ThreePointsConvertedDTO>>(threePointsConverted));
    }

    public ResponseEntity<ResponseMessage<PointsDivisionDTO>> getPointsDivisionByTeamMatches(UUID teamId,
            Integer matches) {
        List<Game> gamesFound = gameRepo.findTopGames(teamId, matches);

        if (gamesFound.isEmpty()) {
            return ResponseEntity.status(204).body(new ResponseMessage<>("Sem jogos registrados nesse time"));
        }

        String gameIdListRequestParam = "";

        for (Game game : gamesFound) {
            gameIdListRequestParam += "gamesIdList=" + game.getId();
        }

        try {
            AthleteHistoricList athleteHistoricListFound = athleteHistoricListService.exchange("3000",
                    "athlete-historics/ms-by-games", teamId, gameIdListRequestParam,
                    AthleteHistoricList.class);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(new ResponseMessage<>("Serviço de usuários fora do ar no momento", e.getMessage()));
        }

        return ResponseEntity.status(200).build();
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public class AthleteHistoricList {
        private List<AthleteHistoric> athleteHistorics;
    }
}