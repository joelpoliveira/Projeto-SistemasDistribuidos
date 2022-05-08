package projetosd.frontend.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import projetosd.frontend.forms.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import java.net.*;
import java.util.List;
import java.util.Scanner;
import java.io.*;

@Controller
public class MainController {

    @GetMapping("/")
    public String redirect() {
        return "redirect:/greetings";
    }

    @GetMapping("/greetings")
    public String greeting(Model model) {
        model.addAttribute("name", "World");
        model.addAttribute("othername", "SD");
        return "greetings";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "createUser";
    }

    @GetMapping("/teste")
    public String teste(Model model) {
        URLConnection connection = null;
        InputStream is = null;
        try {
            connection = new URL("http://127.0.0.1:8081/api/tutorials").openConnection();
            // connection.setRequestProperty("header", header1);
            is = connection.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scanner sc = new Scanner(is);
        model.addAttribute("response", sc.nextLine());

        
        // System.out.println(sc.nextLine());
        sc.close();
        return "teste";
    }
}
