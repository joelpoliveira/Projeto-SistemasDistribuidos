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
}
