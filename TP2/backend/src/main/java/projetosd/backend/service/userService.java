package projetosd.backend.service;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import projetosd.backend.repository.*;
import projetosd.backend.models.*;

@Service
public class userService {
    @Autowired
    private UserRepository userRepository;

    public List<User> getAll() {
        List<User> records = new ArrayList<>();
        userRepository.findAll().forEach(records::add);
        return records;
    }

    private User getUser(User user) {
        return userRepository.findByUsername(user.getUsername());
    }

    public User add(User user) {
        userRepository.save(user);
        return getUser(user);
    }

}
