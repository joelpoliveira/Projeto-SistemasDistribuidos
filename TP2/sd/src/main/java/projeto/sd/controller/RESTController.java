package projeto.sd.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import projeto.sd.model.*;
import projeto.sd.service.*;

@RestController
@RequestMapping("rest")
public class RESTController {
    @Autowired
    UserService userService;

    @PostMapping(value = "/register", consumes = { MediaType.APPLICATION_JSON_VALUE })
    public User registerUser(@Valid @RequestBody User user) {
        return userService.add(user);
    }

    @GetMapping(value = "/users", produces = { MediaType.APPLICATION_JSON_VALUE })
    public List<User> getUsers() {
        return userService.getAll();
    }

}