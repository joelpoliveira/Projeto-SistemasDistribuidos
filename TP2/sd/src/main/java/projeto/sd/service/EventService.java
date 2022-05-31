package projeto.sd.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashMap;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import projeto.sd.repository.*;
import projeto.sd.model.*;

@Service
public class EventService {
    @Autowired
    EventRepository eventRepository;

    @Autowired
    TeamService teamService;

    public Event add(Event event) {
        if (event.getName().equals("Fim")) {
            Game game = event.getGame();
            Team A = game.getTeamA();
            Team B = game.getTeamB();

            // set game as ended
            game.setHasEnded(true);

            // determine winner/looser and update count
            if (game.getTeamAScore() > game.getTeamBScore()) {
                A.setWins(A.getWins() + 1);
                B.setLosses(B.getLosses() + 1);
            } else if (game.getTeamAScore() < game.getTeamBScore()) {
                B.setWins(B.getWins() + 1);
                A.setLosses(A.getLosses() + 1);
            } else {
                A.setTies(A.getTies() + 1);
                B.setTies(B.getTies() + 1);
            }
        }

        if (event.getName().equals("Golo")) {
            Game game = event.getGame();
            Team A = game.getTeamA();
            Team B = game.getTeamB();
            Team t = event.getTeam();
            Player p = event.getPlayer();

            // update game score and team goals
            if (A == t) {
                game.setTeamAScore(game.getTeamAScore() + 1);
                A.setGoals(A.getGoals() + 1);
            } else {
                game.setTeamBScore(game.getTeamBScore() + 1);
                B.setGoals(B.getGoals() + 1);
            }

            // update player goals

            p.setGoals(p.getGoals() + 1);
        }

        eventRepository.save(event);
        return event;
    }

    public List<String> getPossibleEvents() {
        List<String> events = new ArrayList<String>();
        events.add("Início");
        events.add("Fim");
        events.add("Golo");
        events.add("Cartão Amarelo");
        events.add("Cartão Vermelho");
        events.add("Interrupção");
        events.add("Retoma");
        return events;
    }

    public List<Event> getEventsByGameId(int id) {
        return eventRepository.findByGameId(id);
    }

    public HashMap<String, Set<Player>> getPlayersByTeam() {
        HashMap<String, Set<Player>> response = new HashMap<>();

        for (Team t : teamService.getAllTeams()) {
            response.put(t.getName(), t.getPlayers());
        }

        return response;
    }

    public boolean checkEnd(Game game) {
        List<Event> events = getEventsByGameId(game.getId());
        for (Event event : events) {
            if (event.getName().equals("Fim")) {
                return true;
            }
        }
        return false;
    }
}
