package projeto.sd.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import projeto.sd.service.*;
import projeto.sd.model.*;

@Controller
@RequestMapping("/team")
public class TeamController {
    @Autowired
    TeamService teamService;

    @Autowired
    PlayerService playerService;

    @GetMapping("/create")
    public String createTeam(Model model, HttpSession session) {
        model.addAttribute("team", new Team());
        return "create-team";
    }

    @PostMapping("/create")
    public String teamView(@Valid @ModelAttribute Team team, HttpSession session, Model model) {
        Optional<Team> tempTeam = null;
        try {
            tempTeam = teamService.getTeam(team.getId());
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        if (tempTeam.isEmpty()) {
            teamService.add(team);
            System.out.println("Added team:" + team);
            return "redirect:/home";
        } else {
            System.out.println("Team already exists");
            model.addAttribute("error", "Team already exists");
            return "redirect:/create-team?error";
        }

    }

    @GetMapping("/all")
    public String showTeams(Model model, HttpSession session) {
        model.addAttribute("teams", teamService.getAllTeams());
        model.addAttribute("players", playerService.getAllPlayers());
        return "all-teams";
    }

    @GetMapping("/{teamID}")
    public String showTeam(@PathVariable String teamID, Model model) {
        try {
            model.addAttribute("team", teamService.getTeam(Integer.parseInt(teamID)).get());
        } catch (NumberFormatException | NotFoundException | NoSuchElementException e) {
            System.out.println("Team not Found: " + teamID);
            return "redirect:/team/all?noTeam";
        }
        model.addAttribute("players", teamService.getTeamPlayers(Integer.parseInt(teamID)));
        return "team";
    }
}
