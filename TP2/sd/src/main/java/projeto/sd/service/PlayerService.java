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
public class PlayerService {
    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    TeamRepository teamRepository;

    public Player getPlayer(String name) {
        return playerRepository.findByName(name);
    }

    public Player add(Player player) {
        playerRepository.save(player);
        return player;
    }

    public List<Player> addAll(List<Player> players) {
        for (Player player : players) {
            System.out.println(player);
            add(player);
        }
        return players;
    }

}
