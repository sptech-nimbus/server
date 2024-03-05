package com.events.events.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.events.events.domains.game.Game;

public interface GameRepository extends JpaRepository<Game, String>  {
    @Query("")
    List<Game> findGamesByChallengerOrChallenged(String challenger, String challenged);
}