package com.user.user.domain.gameResult;

import java.util.UUID;

import com.user.user.domain.game.Game;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameResult {
    private UUID id;
    private Game game;
    private Integer challengerPoints;
    private Integer challengedPoints;
    private Boolean confirmed;

    @Override
    public String toString() {
        return "\n  GameResult [id=" + id + ", \ngame=" + game + ", \nchallengerPoints=" + challengerPoints
                + ", \nchallengedPoints=" + challengedPoints + ", \nconfirmed=" + confirmed + "]";
    }
}