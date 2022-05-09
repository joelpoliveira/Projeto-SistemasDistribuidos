package projetosd.backend.controller;

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

import projetosd.backend.models.*;
import projetosd.backend.service.*;

@RestController
@RequestMapping("/rest")
public class RESTController {

    @Autowired
    userService userService;

    @PostMapping(value = "/register", consumes = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<Object> registerUser(@Valid @RequestBody User user) {
        Map<String, Object> response = new HashMap<String, Object>();
        APIUser newUser = userService.add(user);

        if (newUser.getId() == 0) {
            response.put("error", "User Already exists");
            return ResponseHandler.generateResponse(response, HttpStatus.GONE);
        } else {
            response.put("response", newUser);
            return ResponseHandler.generateResponse(response, HttpStatus.OK);
        }
    }

    @GetMapping(value = "/users", produces = { MediaType.APPLICATION_JSON_VALUE })
    public List<User> getUsers() {
        return userService.getAll();
    }

    @GetMapping(value = "/teste", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<Object> teste() {
        Map<String, Object> response = new HashMap<String, Object>();
        response.put("response", userService.getAll());
        return ResponseHandler.generateResponse(response, HttpStatus.OK);
    }

}