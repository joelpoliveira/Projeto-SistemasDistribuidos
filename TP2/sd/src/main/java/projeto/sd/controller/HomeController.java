
package projeto.sd.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import projeto.sd.service.*;


@Controller
public class HomeController {
    @Autowired
    GameService gameService;

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

}
