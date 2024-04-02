package com.user.user.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.user.user.domain.athlete.Athlete;
import com.user.user.domain.injury.Injury;

public interface InjuryRepository extends JpaRepository<Injury, UUID> {
    List<Injury> findAllByAthlete(Athlete athlete);
}
