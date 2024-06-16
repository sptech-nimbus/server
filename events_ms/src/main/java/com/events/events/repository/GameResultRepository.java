package com.events.events.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.events.events.domain.gameResult.GameResult;

public interface GameResultRepository extends JpaRepository<GameResult, UUID> {
    @Query(nativeQuery = true, value = "SELECT TOP (:matches) gr.game_result_id, gr.challenged_points, gr.challenger_points, g.* FROM game_result gr LEFT JOIN game g on gr.game_id = g.game_id WHERE g.challenger_id = :teamId OR g.challenged_id = :teamId ORDER BY g.final_date_time DESC")
    List<GameResult> findGameResultsByTeamWithLimit(UUID teamId, Integer matches);

    List<GameResult> findByGameChallengedAndConfirmedFalse(UUID teamId);
}