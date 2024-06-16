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

    @Query(value = "select g.* from game g where challenger_id = ?1 or challenged_id = ?1 limit ?2", nativeQuery = true)
    List<Game> findGamesByChallengerChallengedWithTop(UUID teamId, Integer matches);

    @Query(value = "SELECT TOP (:top) g.* FROM game g WHERE g.challenger_id = :teamId OR challenged_id = :teamId ORDER BY g.final_date_time DESC")
    List<Game> findTopGames(UUID teamId, Integer top);

    @Query("select g from game g where g.challenger = :teamId or challenged = :teamId and g.finalDateTime <= :now order by g.inicialDateTime limit 1")
    Optional<Game> findLastGameByTeam(UUID teamId, LocalDateTime now);

    @Query("select g from game g where g.challenger = :teamId or challenged = :teamId and g.inicialDateTime >= :now order by g.finalDateTime limit 1")
    Optional<Game> findNextGameByTeam(UUID teamId, LocalDateTime now);
}