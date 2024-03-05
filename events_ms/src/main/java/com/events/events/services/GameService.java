package com.events.events.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.events.events.domains.game.Game;
import com.events.events.domains.game.GameDTO;
import com.events.events.domains.responseMessage.ResponseMessage;
import com.events.events.repositories.GameRepository;

@SuppressWarnings("rawtypes")
@Service
public class GameService {
    @Autowired
    GameRepository repo;

    public ResponseEntity<ResponseMessage> register(GameDTO dto) {
        Game newGame = new Game(dto);

        repo.save(newGame);

        return ResponseEntity.ok(new ResponseMessage<Game>(newGame));
    }
}
