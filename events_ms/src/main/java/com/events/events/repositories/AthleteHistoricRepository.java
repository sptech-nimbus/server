package com.events.events.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.events.events.domains.athleteHistoric.AthleteHistoric;

public interface AthleteHistoricRepository extends JpaRepository<AthleteHistoric, UUID> {

}