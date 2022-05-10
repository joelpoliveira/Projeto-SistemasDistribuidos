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

    public Team getTeam(Team team) {
        return teamRepository.findByName(team.getName());
    }

    public Team add(Team team) {
        teamRepository.save(team);
        return getTeam(team);
    }
    
}
