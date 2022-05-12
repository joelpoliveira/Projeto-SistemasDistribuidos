package projeto.sd.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import org.json.*;
import java.net.*;
import java.io.*;

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
        Team tempTeam = teamService.getTeam(team.getName());
        if (tempTeam == null) {
            teamService.add(team);
            //model.addAttribute("error", "");
            System.out.println("Added team:" + team);
            return "redirect:/home";
        } else {
            System.out.println("Team already exists");
            model.addAttribute("error", "Team already exists");
            return "create-team";
        }

    }

    @GetMapping("/show/all")
    public String showTeams(Model model, HttpSession session) {
        model.addAttribute("teams", teamService.getAllTeams());
        model.addAttribute("players", playerService.getAllPlayers());
        return "team";
    }
}
