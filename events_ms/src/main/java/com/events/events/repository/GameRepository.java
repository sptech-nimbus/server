package com.events.events.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.events.events.domain.game.Game;

public interface GameRepository extends JpaRepository<Game, UUID> {
    List<Game> findGamesByChallengerOrChallenged(UUID challenger, UUID challenged);

    @Query(value = "select top (:matches) from game g where challenger_id = :teamId or challenged_id = :teamId order by final_date_time desc", nativeQuery = true)
    List<Game> findTopGamesDesc(UUID teamId, Integer matches);

    @Query("select g from game g where challenger = :teamId or challenged = :teamId and finalDateTime > CURRENT_TIMESTAMP order by finalDateTime")
    List<Game> findNextGames(UUID teamId);

    @Query("select g from game g where g.challenger = :teamId or challenged = :teamId and g.finalDateTime <= :now order by g.inicialDateTime limit 1")
    Optional<Game> findLastGameByTeam(UUID teamId, LocalDateTime now);

    @Query("select g from game g where g.challenger = :teamId or challenged = :teamId and g.inicialDateTime >= :now order by g.finalDateTime limit 1")
    Optional<Game> findNextGameByTeam(UUID teamId, LocalDateTime now);

    @Query("select g from game g where challenger = :teamId or challenged = :teamId")
    List<Game> findGamesByTeamId(UUID teamId); 

    @Query("select g from game g where challenger = :teamId or challenged = :teamId and finalDateTime < CURRENT_TIMESTAMP order by finalDateTime desc")
    List<Game> findLastGames(UUID teamId);

}