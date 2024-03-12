package com.user.user.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.user.user.domains.athlete.Athlete;

public interface AthleteRepository extends JpaRepository<Athlete, UUID> {
    Athlete findAthleteByUserId(UUID userId);

    List<Athlete> findByTeamId(UUID teamId);
}