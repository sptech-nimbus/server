package com.events.events.repositories;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.events.events.domains.training.Training;

public interface TrainingRepository extends JpaRepository<Training, UUID> {
    @Query(nativeQuery = true, value = "SELECT * FROM training WHERE team_id = ?1 AND (?2 <= final_date_time AND ?3 >= inicial_date_time)")
    Optional<Training> findTrainingByTeamAndDate(UUID teamId, LocalDateTime inicialDatetime,
            LocalDateTime finalDateTime);
}