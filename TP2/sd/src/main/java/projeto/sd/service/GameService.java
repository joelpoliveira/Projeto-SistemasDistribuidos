package projeto.sd.service;

import java.util.List;
import java.util.Optional;
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

    public Game add(Game game) {
        gameRepository.save(game);
        return game;
    }

    public List<Game> getAllGames(){
        return gameRepository.findAll();
    }

    public Game getById(int id){
        return gameRepository.findById(id);
    }

}
