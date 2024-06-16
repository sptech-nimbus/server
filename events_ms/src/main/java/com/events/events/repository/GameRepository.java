package com.events.events.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.events.events.domain.game.Game;

public interface GameRepository extends JpaRepository<Game, UUID> {
    List<Game> findGamesByChallengerOrChallenged(UUID challenger, UUID challenged);

    @Query(value = "select top (:matches) * from game g where challenger_id = :teamId or challenged_id = :teamId order by final_date_time desc", nativeQuery = true)
    List<Game> findTopGamesDesc(UUID teamId, Integer matches);

    @Query("select g from game g where challenger = :teamId or challenged = :teamId and finalDateTime > CURRENT_TIMESTAMP order by finalDateTime")
    List<Game> findNextGames(UUID teamId);

    @Query("select g from game g where challenger = :teamId or challenged = :teamId and finalDateTime < CURRENT_TIMESTAMP order by finalDateTime desc")
    List<Game> findLastGames(UUID teamId);
}