package com.events.events.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.events.events.domain.game.Game;

public interface GameRepository extends JpaRepository<Game, UUID>  {
    @Query("")
    List<Game> findGamesByChallengerOrChallenged(UUID challenger, UUID challenged);
}