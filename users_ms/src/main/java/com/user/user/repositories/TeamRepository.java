package com.user.user.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.user.user.domains.team.Team;

public interface TeamRepository extends JpaRepository<Team, String> {

}
