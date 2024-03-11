package com.user.user.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.user.user.domains.coach.Coach;

public interface CoachRepository extends JpaRepository<Coach, UUID> {
    Coach findCoachByUserId(UUID userId);
}
