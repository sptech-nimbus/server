package com.events.events.repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.events.events.domain.training.Training;

public interface TrainingRepository extends JpaRepository<Training, UUID> {
    // @Query(nativeQuery = true, value = "SELECT * FROM training WHERE team_id = ?1 AND (?2 <= final_date_time AND ?3 >= inicial_date_time)")
    Optional<Training> findByTeamAndFinalDateTimeBetween(UUID teamId, LocalDateTime inicialDatetime,
            LocalDateTime finalDateTime);

    Page<Training> findAllByTeam(UUID teamId, Pageable pageable);
}