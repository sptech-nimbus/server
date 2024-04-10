package com.user.user.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.user.user.domain.athleteHistoric.AthleteHistoric;

import java.util.List;

public interface AthleteHistoricRepository extends JpaRepository<AthleteHistoric, UUID> {
    List<AthleteHistoric> findByAthleteId(UUID athleteId);

    Page<AthleteHistoric> findAllByAthleteId(UUID athleteId, Pageable pageable);

    List<AthleteHistoric> findByAthleteTeamIdAndGameIdIn(UUID teamId, List<UUID> gamesId);
}