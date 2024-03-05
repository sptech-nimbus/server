package com.events.events.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.events.events.domains.game.Game;

public interface GameRepository extends JpaRepository<Game, String>  {
    
}