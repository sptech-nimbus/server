package com.user.user.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.user.user.models.athleteDesc.AthleteDesc;

public interface AthleteDescRepository extends JpaRepository<AthleteDesc, String> {

}