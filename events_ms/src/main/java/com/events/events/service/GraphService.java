package com.events.events.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.events.events.domain.athleteHistoric.AthleteHistoric;
import com.events.events.domain.game.Game;
import com.events.events.domain.gameResult.GameResult;
import com.events.events.domain.graphs.PointsDivisionDTO;
import com.events.events.domain.graphs.WinsFromTeamDTO;
import com.events.events.domain.responseMessage.ResponseMessage;
import com.events.events.exception.NoContentException;
import com.events.events.repository.GameRepository;
import com.events.events.repository.GameResultRepository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Service
@RequiredArgsConstructor
public class GraphService {
    private final GameResultRepository gameResultRepo;
    private final GameRepository gameRepo;
    private final RestTemplateService<AthleteHistoricList> athleteHistoricListService;
    private final RestTemplateService<AthleteHistoric> athleteHistoricService;

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
            athleteHistoricListService.exchange("3000",
                    "athlete-historics/ms-by-games", teamId, gameIdListRequestParam,
                    AthleteHistoricList.class);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(new ResponseMessage<>("Serviço de usuários fora do ar no momento", e.getMessage()));
        }

        return ResponseEntity.status(200).build();
    }

    public Map<Game, Integer> getPointsPerGame(UUID teamId, Integer matches) {
        List<GameResult> gameResultsFound = gameResultRepo.findGameResultsByTeamWithLimit(teamId, matches);

        Map<Game, Integer> pointsPerGame = new HashMap<>();

        for (GameResult gr : gameResultsFound) {
            pointsPerGame.put(gr.getGame(),
                    gr.getGame().getChallenged().equals(teamId)
                            ? gr.getChallengedPoints()
                            : gr.getChallengerPoints());
        }

        return pointsPerGame;
    }

    public Map<Game, Integer> getFoulsPerGame(UUID teamId, Integer matches) {
        List<Game> gamesFound = gameRepo.findTopGames(teamId, matches);

        if (gamesFound.isEmpty()) {
            throw new NoContentException();
        }

        String gameIdListRequestParam = "";

        for (Game game : gamesFound) {
            if (gameIdListRequestParam != "")
                gameIdListRequestParam += "&";

            gameIdListRequestParam += "gamesIdList=" + game.getId();
        }

        AthleteHistoric[] athleteHistoricsArray = athleteHistoricService.getTemplateList("3000",
                "athlete-historics/ms-by-games", teamId, gameIdListRequestParam, AthleteHistoric[].class);

        List<AthleteHistoric> athleteHistoricList = new ArrayList<AthleteHistoric>(
                Arrays.asList(athleteHistoricsArray));

        System.out.println(athleteHistoricList);

        Map<Game, List<AthleteHistoric>> mapAthleteHistoricPerGame = new HashMap<Game, List<AthleteHistoric>>();

        for (Game game : gamesFound) {
            List<AthleteHistoric> athleteHistorics = athleteHistoricList.stream()
                    .filter(g -> g.getGameId().equals(game.getId()))
                    .toList();

            mapAthleteHistoricPerGame.put(game, athleteHistorics);
        }

        Map<Game, Integer> mapFoulsPerGame = new HashMap<Game, Integer>();

        for (Game game : mapAthleteHistoricPerGame.keySet()) {
            Integer totalFouls = 0;
            for (AthleteHistoric ah : mapAthleteHistoricPerGame.get(game)) {
                totalFouls += ah.getFouls();
            }

            mapFoulsPerGame.put(game, totalFouls);
        }

        return mapFoulsPerGame;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public class AthleteHistoricList {
        private List<AthleteHistoric> athleteHistorics;
    }
}