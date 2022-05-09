package projeto.sd.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import org.springframework.web.bind.annotation.ModelAttribute;

import org.json.*;
import java.net.*;
import java.io.*;

import projeto.sd.service.*;
import projeto.sd.model.*;

@Controller
public class DATAController {
    @Autowired
    UserService userService;

    @GetMapping("/")
    public String redirect() {
        return "hello";
    }

    @PostMapping("/register")
    public String createUser(@Valid @ModelAttribute User user, BindingResult result, RedirectAttributes ra) {

        if (result.hasErrors()) {
            return "register";
        }
    
        User newUser = userService.getUser(user);
        if (newUser == null) {
            System.out.println("Created new User: " + user);
            userService.add(user);
            return "hello";
        } else {
            System.out.println("Username already exists");
            // errors.rejectValue("username", "Invalid");
            ra.addFlashAttribute("username", "Invalid");
            result.addError(new ObjectError("username", "Cenas"));
            return "redirect:/register";
        }
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

}