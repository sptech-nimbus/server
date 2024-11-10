package com.user.user.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.user.user.domain.athleteHistoric.AthleteHistoric;

public interface AthleteHistoricRepository extends JpaRepository<AthleteHistoric, UUID> {
    List<AthleteHistoric> findByAthleteId(UUID athleteId);

    Page<AthleteHistoric> findAllByAthleteId(UUID athleteId, Pageable pageable);

    List<AthleteHistoric> findByAthleteTeamIdAndGameIdIn(UUID teamId, List<UUID> gamesId);

    @Query("SELECT ah FROM athlete_historic ah WHERE ah.athlete.team.id  = :teamId")
    List<AthleteHistoric> findByTeamId(UUID teamId);
}