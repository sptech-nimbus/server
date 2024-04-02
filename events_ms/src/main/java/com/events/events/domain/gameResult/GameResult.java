package com.events.events.domain.gameResult;

import java.util.UUID;

import com.events.events.domain.game.Game;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "game_result")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameResult {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "game_result_id")
    private UUID id;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "game_id", referencedColumnName = "game_id")
    private Game game;

    @Column(name = "challenger_points")
    private Integer challengerPoints;

    @Column(name = "challenged_points")
    private Integer challengedPoints;

    @Column(name = "confirmed")
    private Boolean confirmed;

    @Override
    public String toString() {
        return "\n  GameResult [id=" + id + ", \ngame=" + game + ", \nchallengerPoints=" + challengerPoints
                + ", \nchallengedPoints=" + challengedPoints + ", \nconfirmed=" + confirmed + "]";
    }
}