package projeto.sd.controller;

import java.util.List;

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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import projeto.sd.service.*;
import projeto.sd.model.*;

@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    TeamService teamService;

    @Autowired
    UserService userService;

    @Autowired
    PlayerService playerService;



    @PostMapping("/users")
    public List<User> createUsers(@Valid @RequestBody List<User> users) {
        userService.addAll(users);  
        return users;
    }

    @PostMapping("/players")
    public List<Player> createPLayers(@Valid @RequestBody List<Player> players) {
        playerService.addAll(players);  
        return players;
    }

    @PostMapping("/teams")
    public List<Team> createTeams(@Valid @RequestBody List<Team> teams) {
        teamService.addAll(teams);
        return teams;
    }

    // @PostMapping("/teste")
    // public Team teste() {
    //     Team team = teamService.getTeam("Sporting");
    //     return team;
    // }

    
}
