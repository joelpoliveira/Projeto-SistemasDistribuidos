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

    public Team getTeam(String name) {
        return teamRepository.findByName(name);
    }

    public List<Player> getAllPlayers(Team team) {
        return teamRepository.findPlayersByName(team.getName());
    }

    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    public List<Player> getTeamPlayers(String teamName) {
        return teamRepository.findPlayersByTeamID(getTeam(teamName).getId());
    }

}
