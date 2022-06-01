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

import projeto.sd.service.*;
import projeto.sd.model.*;

@Controller
@RequestMapping("/statistics")
public class StatisticsController {
    @Autowired
    EventService eventService;

    @Autowired
    TeamService teamService;

    @Autowired
    PlayerService playerService;


    @GetMapping("/")
    public String createTeam(Model model, HttpSession session) {
        teamService.updateBestScorer();
        model.addAttribute("teams", teamService.getAllTeams());
        return "statistics";
    }


}
