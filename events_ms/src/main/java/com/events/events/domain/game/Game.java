package com.events.events.domain.game;

import java.time.LocalDateTime;
import java.util.UUID;

import com.events.events.domain.gameResult.GameResult;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
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

    @Column(name = "confirmed")
    private Boolean confirmed;

    @Column(name = "inicial_date_time")
    private LocalDateTime inicialDateTime;

    @Column(name = "final_date_time")
    private LocalDateTime finalDateTime;

    @Column(name = "local")
    private String local;

    @Column(name = "challenger_id", nullable = false)
    private UUID challenger;

    @Column(name = "challenged_id", nullable = false)
    private UUID challenged;

    @OneToOne(mappedBy = "game")
    private GameResult gameResult;

    public Game(GameDTO dto) {
        this.setChallenged(dto.challenged());
        this.setChallenger(dto.challenger());
        this.setFinalDateTime(dto.finalDateTime());
        this.setInicialDateTime(dto.inicialDateTime());
        this.setLocal(dto.local());
        this.setConfirmed(false);
    }

    @Override
    public String toString() {
        return "Game {" + '\'' +
                "id=" + id + '\'' +
                "confirmed=" + confirmed + '\'' +
                "inicialDateTime=" + inicialDateTime + '\'' +
                "finalDateTime=" + finalDateTime + '\'' +
                "local=" + local + '\'' +
                "challenger=" + challenger + '\'' +
                "challenged=" + challenged + '\'' +
                "}";
    }
}