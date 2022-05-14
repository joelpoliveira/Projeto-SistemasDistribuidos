
package projeto.sd.controller;

import java.lang.reflect.Array;
import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import projeto.sd.model.Game;
import projeto.sd.model.Player;
import projeto.sd.model.Team;
import projeto.sd.service.*;

@Controller
public class HomeController {
    @Autowired
    GameService gameService;

    @Autowired
    TeamService teamService;

    @Autowired
    PlayerService playerService;

    @GetMapping("/home")
    public String home() {
        return "redirect:/";
    }

    @GetMapping("/")
    public String homepage(Model model, HttpSession session) {
        model.addAttribute("games", gameService.getAllGames());

        if (session.getAttribute("username") != null)
            model.addAttribute("username", session.getAttribute("username"));
        else
            model.addAttribute("username", "");
        return "index";
    }

    @GetMapping("/fill")
    public String fillDatabase() {
        ArrayList<Team> teams = new ArrayList<>();
        ArrayList<String> equipas = new ArrayList<>();
        equipas.add("Sporting");
        equipas.add("Benfica");
        equipas.add("Porto");
        equipas.add("Boavista");
        equipas.add("Maritimo");
        equipas.add("Rio Ave");

        for (String e : equipas) {
            Team t = new Team(e, "");
            teamService.add(t);
            teams.add(t);
        }

        for (Team t : teams) {
            playerService.add(new Player("A", "Striker", null, t));
            playerService.add(new Player("B", "Striker", null, t));
            playerService.add(new Player("C", "Striker", null, t));
            playerService.add(new Player("D", "Striker", null, t));
        }

        gameService.add(new Game("Lisboa", null, null, teams.get(0), teams.get(1)));
        gameService.add(new Game("Lisboa", null, null, teams.get(0), teams.get(2)));
        gameService.add(new Game("Lisboa", null, null, teams.get(3), teams.get(1)));
       
        return "redirect:/";
    }

}
