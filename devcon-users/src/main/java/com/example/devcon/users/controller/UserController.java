package com.example.devcon.users.controller;

import com.example.devcon.users.model.User;
import com.example.devcon.users.model.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/")
    public ResponseEntity<List<User>> list() {
        final List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/")
    public ResponseEntity<User> save(@RequestBody User user) {
        return ResponseEntity.ok(userRepository.save(user));
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable long id) {
        userRepository.deleteById(id);
        return ResponseEntity.ok(true);
    }


}
