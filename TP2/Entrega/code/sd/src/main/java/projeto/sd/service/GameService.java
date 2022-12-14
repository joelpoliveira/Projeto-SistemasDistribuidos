package projeto.sd.service;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;
import java.util.ArrayList;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import projeto.sd.repository.*;
import projeto.sd.model.*;

@Service
public class GameService {
    @Autowired
    GameRepository gameRepository;

    @Autowired
    EventService eventService;

    public Game add(Game game) {
        gameRepository.save(game);
        return game;
    }

    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }

    public List<Game> getAllActiveGames() {
        List<Game> games = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        for (Game game : gameRepository.findAll()) {
            try {
                if (now.isAfter(game.getStartTime()) && !game.getHasEnded()) {
                    games.add(game);
                }
            } catch (NullPointerException e) {
                // game with null date
                // System.out.println("Showing game with null date");
            }
        }
        return games;
    }

    public List<Game> getAllNonActiveGames() {
        List<Game> games = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        for (Game game : gameRepository.findAll()) {
            try {
                // Non active games -> game has ended or 4 hours since game start have passed
                // just in case peole don't report the end of the game
                if (game.getHasEnded() || game.getStartTime().plusHours(4).isBefore(now)) {
                    // check se tem o evento de Fim
                    if (!eventService.checkEnd(game)) {
                        // adicionar o evento Fim, caso nao tenha sido adicionado
                        eventService.add(new Event("Fim", game, null, null));
                    }
                    games.add(game);
                }
            } catch (NullPointerException e) {
                // game with null date
                // System.out.println("Showing game with null date");
            }
        }
        return games;
    }

    public Game getById(int id) {
        return gameRepository.findById(id);
    }

}
