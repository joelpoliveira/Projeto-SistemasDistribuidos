package projeto.sd.service;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

import projeto.sd.repository.*;
import projeto.sd.model.*;

@Service
public class TeamService {
    @Autowired
    TeamRepository teamRepository;

    public Team add(Team team) {
        teamRepository.save(team);
        return team;
    }

    public List<Team> addAll(List<Team> teams) {
        for (Team team : teams) {
            teamRepository.save(team);
        }
        return teams;
    }

    public Optional<Team> getTeam(int teamID) throws NotFoundException {
        return teamRepository.findById(teamID);
    }

    public Optional<Team> getTeam(String teamName) throws NotFoundException {
        return teamRepository.findByName(teamName);
    }

    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    public List<Player> getTeamPlayers(int teamID) {
        return teamRepository.findPlayersByTeamID(teamID);
    }

    public void updateBestScorer() {
        List<Player> players;
        int best;

        for (Team t : getAllTeams()) {
            best = 0;
            players = getTeamPlayers(t.getId());
            for (Player p : players) {
                if (p.getGoals() > best) {
                    t.setBestScorer(p);
                }
            }
        }
    }

}
