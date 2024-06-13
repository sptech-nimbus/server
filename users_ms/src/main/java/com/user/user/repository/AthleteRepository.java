package com.user.user.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.user.user.domain.athlete.Athlete;

public interface AthleteRepository extends JpaRepository<Athlete, UUID> {
    Optional<Athlete> findAthleteByUserId(UUID userId);

    List<Athlete> findByTeamId(UUID teamId);

    Boolean existsByUserId(UUID userId);

    List<Athlete> findAllByTeam_Id(UUID teamId);
}