package com.events.events.service;

import java.time.LocalDateTime;
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
import com.events.events.domain.graphs.ReboundsPerTeam;
import com.events.events.domain.graphs.WinsFromTeamDTO;
import com.events.events.domain.responseMessage.ResponseMessage;
import com.events.events.exception.NoContentException;
import com.events.events.repository.GameRepository;
import com.events.events.repository.GameResultRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GraphService {
    private final GameResultRepository gameResultRepo;
    private final GameRepository gameRepo;
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

    public Map<Game, ReboundsPerTeam> getReboundsPerGameFromTeam(UUID teamId, Integer matches) {
        List<Game> gamesFound = gameRepo.findTopGamesDesc(teamId, matches);

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

        List<AthleteHistoric> athleteHistoricList = new ArrayList<>(Arrays.asList(athleteHistoricsArray));

        Map<Game, List<AthleteHistoric>> mapAthleteHistoricPerGame = getHistoricsPerGameByGamesAndHistoricList(
                gamesFound, athleteHistoricList);

        Map<Game, ReboundsPerTeam> mapReboundsPerGame = new HashMap<>();

        for (Game game : mapAthleteHistoricPerGame.keySet()) {
            mapReboundsPerGame.put(game, new ReboundsPerTeam(
                    getAllOffReboundsFromAthleteHistoricList(mapAthleteHistoricPerGame.get(game)),
                    getAllDefReboundsFromAthleteHistoricList(mapAthleteHistoricPerGame.get(game))));
        }

        return mapReboundsPerGame;
    }

    public PointsDivisionDTO getPointsDivisionByTeamMatches(UUID teamId, Integer matches) {
        List<Game> gamesFound = gameRepo.findGamesByChallengerOrChallenged(teamId, teamId);

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

        List<AthleteHistoric> athleteHistoricList = new ArrayList<>(Arrays.asList(athleteHistoricsArray));

        Map<Game, List<AthleteHistoric>> mapAthleteHistoricPerGame = getHistoricsPerGameByGamesAndHistoricList(
                gamesFound, athleteHistoricList);

        Integer totalTwoPoints = 0, totalThreePoints = 0;

        for (Game game : mapAthleteHistoricPerGame.keySet()) {
            totalTwoPoints += getAllTwoPointsFromAthleteHistoricList(mapAthleteHistoricPerGame.get(game));
            totalThreePoints += getAllThreePointsFromAthleteHistoricList(mapAthleteHistoricPerGame.get(game));
        }

        Integer totalPoints = totalTwoPoints + totalThreePoints;

        Double twoPointsPorcentage = ((double) totalTwoPoints / totalPoints) * 100;
        Double threePointsPorcentage = ((double) totalThreePoints / totalPoints) * 100;

        return new PointsDivisionDTO(twoPointsPorcentage, threePointsPorcentage);
    }

    public Integer getAllTwoPointsFromAthleteHistoricList(List<AthleteHistoric> ahs) {
        Integer totalTwoPoints = 0;

        for (AthleteHistoric ah : ahs) {
            totalTwoPoints += ah.getTwoPointsConverted();
        }

        return totalTwoPoints;
    }

    public Integer getAllDefReboundsFromAthleteHistoricList(List<AthleteHistoric> ahs) {
        Integer totalDefReboundsPoints = 0;

        for (AthleteHistoric ah : ahs) {
            totalDefReboundsPoints += ah.getDefRebounds();
        }

        return totalDefReboundsPoints;
    }

    public Integer getAllOffReboundsFromAthleteHistoricList(List<AthleteHistoric> ahs) {
        Integer totalOffReboundsPoints = 0;

        for (AthleteHistoric ah : ahs) {
            totalOffReboundsPoints += ah.getOffRebounds();
        }

        return totalOffReboundsPoints;
    }

    public Integer getAllThreePointsFromAthleteHistoricList(List<AthleteHistoric> ahs) {
        Integer totalThreePoints = 0;

        for (AthleteHistoric ah : ahs) {
            totalThreePoints += ah.getThreePointsConverted();
        }

        return totalThreePoints;
    }

    public Map<LocalDateTime, Integer> getPointsPerGame(UUID teamId, Integer matches) {
        List<GameResult> gameResultsFound = gameResultRepo.findGameResultsByTeamWithLimit(teamId, matches);

        Map<LocalDateTime, Integer> pointsPerGame = new HashMap<>();

        for (GameResult gr : gameResultsFound) {
            pointsPerGame.put(gr.getGame().getInicialDateTime(),
                    gr.getGame().getChallenged().equals(teamId)
                            ? gr.getChallengedPoints()
                            : gr.getChallengerPoints());
        }

        return pointsPerGame;
    }

    public Map<LocalDateTime, Integer> getFoulsPerGame(UUID teamId, Integer matches) {
        List<Game> gamesFound = gameRepo.findTopGamesDesc(teamId, matches);

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

        Map<LocalDateTime, Integer> mapFoulsPerGame = new HashMap<LocalDateTime, Integer>();

        Map<Game, List<AthleteHistoric>> mapAthleteHistoricPerGame = getHistoricsPerGameByGamesAndHistoricList(
                gamesFound, athleteHistoricList);

        for (Game game : mapAthleteHistoricPerGame.keySet()) {
            Integer totalFouls = 0;

            for (AthleteHistoric ah : mapAthleteHistoricPerGame.get(game)) {
                totalFouls += ah.getFouls();
            }

            mapFoulsPerGame.put(game.getFinalDateTime(), totalFouls);
        }

        return mapFoulsPerGame;
    }

    public Map<Game, List<AthleteHistoric>> getHistoricsPerGameByGamesAndHistoricList(List<Game> games,
            List<AthleteHistoric> athleteHistorics) {
        Map<Game, List<AthleteHistoric>> mapAthleteHistoricPerGame = new HashMap<Game, List<AthleteHistoric>>();

        for (Game game : games) {
            List<AthleteHistoric> ah = athleteHistorics.stream()
                    .filter(g -> g.getGameId().equals(game.getId()))
                    .toList();

            mapAthleteHistoricPerGame.put(game, ah);
        }

        return mapAthleteHistoricPerGame;
    }
}