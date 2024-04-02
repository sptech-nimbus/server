package com.events.events.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.events.events.domain.gameResult.GameResult;

public interface GameResultRepository extends JpaRepository<GameResult, UUID> {
    @Query(nativeQuery = true, value = "select gr.game_result_id, gr.challenged_points, gr.challenger_points, g.* from game_result gr left join game g on gr.game_id = g.game_id where g.challenger_id = ?1 or g.challenged_id = ?1 order by g.final_date_time desc limit ?2")
    List<GameResult> findGameResultsByTeamWithLimit(UUID teamId, Integer matches);
}