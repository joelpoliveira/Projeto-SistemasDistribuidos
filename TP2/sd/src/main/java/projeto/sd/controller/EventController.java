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

import java.net.*;
import java.time.LocalDateTime;
import java.io.*;

import projeto.sd.service.*;
import projeto.sd.model.*;

@Controller
@RequestMapping("/event")
public class EventController {
    @Autowired
    EventService eventService;

    @Autowired
    GameService gameService;

    @GetMapping("/create")
    public String createTeam(Model model, HttpSession session) {
        model.addAttribute("event", new Event());
        model.addAttribute("games", gameService.getAllActiveGames());
        model.addAttribute("events", eventService.getPossibleEvents());
        model.addAttribute("teams", eventService.getPlayersByTeam());
        return "create-event";
    }

    @PostMapping("/create")
    public String teamView(@Valid @ModelAttribute Event event, BindingResult result, Model model) {

        if (result.hasErrors()) {
            System.out.println("Erro a criar evento");
            return "redirect:/event/create?error";
        }

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

            if (t == null) {
                System.out.println(t);
                return "redirect:/event/create?noTeamDefined";
            }

            if (p == null) {
                return "redirect:/event/create?noPlayerDefined";
            }

            // update game score and team goals
            if (A == t) {
                game.setTeamAScore(game.getTeamAScore() + 1);
                A.setGoals(A.getGoals() + 1);
            } else {
                game.setTeamBScore(game.getTeamBScore() + 1);
                B.setGoals(B.getGoals() + 1);
            }

            // update player goals
            if (p != null) {
                p.setGoals(p.getGoals() + 1);
            }

        }

        event.setCreated_at(LocalDateTime.now());   
        eventService.add(event);
        System.out.println("Added Event" + event);

        return "redirect:/home";
    }

}
