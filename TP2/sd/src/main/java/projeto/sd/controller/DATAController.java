package projeto.sd.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


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

    @GetMapping("/users")
    public String showAllUsers() {
        try {
            URL url = new URL("http://localhost:8080/rest/users");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            connection.setRequestProperty("Accept", "application/json");

            // TODO read json

            
        } catch (MalformedURLException e){
            System.out.println("Invalid URL: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("URL connection: " + e.getMessage());
        }
        return "users";
    }

}