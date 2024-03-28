package com.user.user.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.user.user.domain.team.Team;

public interface TeamRepository extends JpaRepository<Team, UUID> {

}
