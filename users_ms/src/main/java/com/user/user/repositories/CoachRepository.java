package com.user.user.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.user.user.domains.coach.Coach;

public interface CoachRepository extends JpaRepository<Coach, String> {
    Coach findCoachByUserId(String userId);
}
