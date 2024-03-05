package com.user.user.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.user.user.domains.athlete.Athlete;

public interface AthleteRepository extends JpaRepository<Athlete, String> {
    Athlete findAthleteByUserId(String userId);
}