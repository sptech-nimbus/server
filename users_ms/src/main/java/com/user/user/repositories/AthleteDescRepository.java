package com.user.user.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.user.user.domains.athleteDesc.AthleteDesc;

public interface AthleteDescRepository extends JpaRepository<AthleteDesc, String> {
    Optional<AthleteDesc> findByAthleteId(String athleteId);
}