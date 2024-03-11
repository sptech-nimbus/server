package com.user.user.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.user.user.domains.athleteDesc.AthleteDesc;

public interface AthleteDescRepository extends JpaRepository<AthleteDesc, UUID> {
    Optional<AthleteDesc> findByAthleteId(UUID athleteId);
}