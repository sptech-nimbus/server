package com.user.user.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.user.user.models.injury.Injury;

public interface InjuryRepository extends JpaRepository<Injury, String> {
    
}
