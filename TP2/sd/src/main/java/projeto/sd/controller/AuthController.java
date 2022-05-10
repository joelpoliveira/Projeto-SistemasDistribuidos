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
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    UserService userService;

    @PostMapping("/register")
    public String createUser(@Valid @ModelAttribute User user, HttpSession session) {

        User newUser = userService.getUser(user);
        if (newUser == null) {
            user.setIsAdmin(false);
            userService.add(user);
            session.setAttribute("username", user.getUsername());
            System.out.println("Created new User: " + user);
            return "redirect:/home";
        } else {
            System.out.println("Username already exists");
            return "redirect:/register";
        }
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    @PostMapping("/login")
    public String auth(@Valid @ModelAttribute User user, HttpSession session) {
        User tempUser = userService.getUser(user);
        if (tempUser != null) {
            if (tempUser.getPassword().equals(user.getPassword())) {
                session.setAttribute("username", user.getUsername());
                return "redirect:/home";
            } else {
                System.out.println("Incorrect password");
            }
        } else {
            System.out.println("User doen't exist");
        }

        return "login";
    }

    @GetMapping("/logout")
    public String logout(Model model, HttpSession session) {
        session.removeAttribute("username");
        return "redirect:/home";
    }

}