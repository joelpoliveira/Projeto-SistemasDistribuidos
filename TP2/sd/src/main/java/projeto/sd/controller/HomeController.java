
package projeto.sd.controller;

import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/home")
    public String home() {
        return "redirect:/";
    }

    @GetMapping("/")
    public String homepage(Model model, HttpSession session) {
        if (session.getAttribute("username") != null)
            model.addAttribute("username", session.getAttribute("username"));
        else
            model.addAttribute("username", "");
        return "index";
    }

}
