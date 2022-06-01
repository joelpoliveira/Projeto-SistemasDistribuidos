package projeto.sd.service;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import projeto.sd.repository.*;
import projeto.sd.model.*;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<User> getAll() {
        List<User> records = new ArrayList<>();
        userRepository.findAll().forEach(records::add);
        return records;
    }

    public Optional<User> getUser(User user) {
        return userRepository.findByUsername(user.getUsername());
    }

    public User add(User newUser) {
        userRepository.save(newUser);
        return newUser;

    }

    public List<User> addAll(List<User> users) {
        for (User player : users) {
            userRepository.save(player);
        }
        return users;
    }

}