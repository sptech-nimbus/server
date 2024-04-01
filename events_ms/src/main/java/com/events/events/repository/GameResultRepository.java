package com.events.events.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.events.events.domain.gameResult.GameResult;

public interface GameResultRepository extends JpaRepository<GameResult, UUID> {

}
