package com.user.user.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.user.user.domains.injury.Injury;

public interface InjuryRepository extends JpaRepository<Injury, UUID> {
    
}
