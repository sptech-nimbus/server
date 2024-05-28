package com.user.user.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.user.user.domain.coach.Coach;

public interface CoachRepository extends JpaRepository<Coach, UUID> {
    Optional<Coach> findCoachByUserId(UUID userId);

    Boolean existsByUserId(UUID userId);
}
