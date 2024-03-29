package com.user.user.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.user.user.domain.injury.Injury;

public interface InjuryRepository extends JpaRepository<Injury, UUID> {
    
}
