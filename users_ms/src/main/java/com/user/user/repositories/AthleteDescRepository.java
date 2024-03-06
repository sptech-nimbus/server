package com.user.user.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.user.user.domains.athleteDesc.AthleteDesc;

public interface AthleteDescRepository extends JpaRepository<AthleteDesc, String> {
    AthleteDesc findByAthleteId(String athleteId);
}