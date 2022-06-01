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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.net.*;
import java.io.*;

import projeto.sd.service.*;
import projeto.sd.model.*;
import projeto.sd.repository.GameRepository;

@Controller
@RequestMapping("/game")
public class GameController {
    @Autowired
    GameService gameService;

    @Autowired
    TeamService teamService;

    @Autowired
    EventService eventService;

    @GetMapping("/create")
    public String createTeam(Model model, HttpSession session) {
        model.addAttribute("game", new Game());
        model.addAttribute("teams", teamService.getAllTeams());
        return "create-game";
    }

    @PostMapping("/create")
    public String teamView(@Valid @ModelAttribute Game game, BindingResult result, Model model) {

        if (result.hasErrors()){
            return "redirect:/game/create?error";
        }
        
        gameService.add(game);
        System.out.println("Added game" + game);
        return "redirect:/home";
    }

    @GetMapping("/all")
    public String showAllGames(Model model) {
        model.addAttribute("games", gameService.getAllGames());
        return "redirect:/home";
    }

    @GetMapping("/{gameID}")
    public String showGame(@PathVariable String gameID, Model model) {
        model.addAttribute("game", gameService.getById(Integer.parseInt(gameID)));
        model.addAttribute("events", eventService.getEventsByGameId(Integer.parseInt(gameID)));
        return "game";
    }
}
