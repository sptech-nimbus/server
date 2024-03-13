package com.events.events.domains.game;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.events.events.domains.athleteHistoric.AthleteHistoric;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "game")
@Table(name = "game")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "game_id")
    private UUID id;

    @Column(name = "confirmed", columnDefinition = "boolean default 0")
    private Boolean confirmed;

    @Column(name = "date_time", nullable = false)
    private LocalDateTime dateTime;

    @Column(name = "local")
    private String local;

    @Column(name = "challenger_id", nullable = false)
    private UUID challenger;

    @Column(name = "challenged_id", nullable = false)
    private UUID challenged;

    @OneToMany(mappedBy = "game")
    private List<AthleteHistoric> athletesHistorics;

    public Game(GameDTO dto) {
        this.setChallenged(dto.challenged());
        this.setChallenger(dto.challenger());
        this.setDateTime(dto.dateTime());
        this.setLocal(dto.local());
        this.setConfirmed(false);
    }
}