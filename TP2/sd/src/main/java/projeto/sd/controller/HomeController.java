
package projeto.sd.controller;

import java.lang.reflect.Array;
import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import projeto.sd.model.*;
import projeto.sd.service.*;

@Controller
public class HomeController {
    @Autowired
    GameService gameService;

    @Autowired
    TeamService teamService;

    @Autowired
    PlayerService playerService;

    @Autowired
    UserService userService;

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

    @GetMapping("/403")
    public String AcessDenied() {
       return "403";
    }

    @GetMapping("/fill")
    public String fillDatabase() {
        // Add Admin
        User admin = new User("admin", "admin", "admin@scoredei.pt", null);
        admin.setRoles("ADMIN");
        userService.add(admin);

        ArrayList<Team> teams = new ArrayList<>();
        ArrayList<String> equipas = new ArrayList<>();
        equipas.add("Sporting");
        equipas.add("Benfica");
        equipas.add("Porto");
        equipas.add("Boavista");
        equipas.add("Maritimo");
        equipas.add("Rio Ave");
        equipas.add("Portimonense");

        for (String e : equipas) {
            Team t = new Team(e, "");
            teamService.add(t);
            teams.add(t);
        }
        
        teams.get(0).setImageUrl("https://upload.wikimedia.org/wikipedia/en/thumb/e/e1/Sporting_Clube_de_Portugal_%28Logo%29.svg/1200px-Sporting_Clube_de_Portugal_%28Logo%29.svg.png");
        teams.get(1).setImageUrl("https://upload.wikimedia.org/wikipedia/en/thumb/a/a2/SL_Benfica_logo.svg/1200px-SL_Benfica_logo.svg.png");
        teams.get(2).setImageUrl("https://upload.wikimedia.org/wikipedia/pt/c/c5/F.C._Porto_logo.png");
        teams.get(3).setImageUrl("https://upload.wikimedia.org/wikipedia/pt/5/5c/Logo_Boavista_FC.png");
        teams.get(4).setImageUrl("https://upload.wikimedia.org/wikipedia/pt/a/a2/Logo_CS_Maritimo.png");
        teams.get(5).setImageUrl("https://upload.wikimedia.org/wikipedia/pt/f/f5/Logo_Rio_Ave.png");
        teams.get(6).setImageUrl("https://upload.wikimedia.org/wikipedia/en/thumb/c/c0/Portimonense_Sporting_Clube_logo.svg/1200px-Portimonense_Sporting_Clube_logo.svg.png");

        for (Team t : teams) {
            playerService.add(new Player("A", "Striker", null, t));
            playerService.add(new Player("B", "Striker", null, t));
            playerService.add(new Player("C", "Striker", null, t));
            playerService.add(new Player("D", "Striker", null, t));
        }

        gameService.add(new Game("Lisboa", null, teams.get(0), teams.get(1)));
        gameService.add(new Game("Lisboa", null, teams.get(0), teams.get(2)));
        gameService.add(new Game("Lisboa", null, teams.get(3), teams.get(1)));
       
        return "redirect:/";
    }

}
