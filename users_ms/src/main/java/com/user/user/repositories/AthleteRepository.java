package com.user.user.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.user.user.models.athlete.Athlete;

public interface AthleteRepository extends JpaRepository<Athlete, String> {

}
