package ru.job4j.chat.controller;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.job4j.chat.model.Person;
import ru.job4j.chat.service.ChatService;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    private ChatService chatService;
    private BCryptPasswordEncoder encoder;

    public UserController(ChatService chatService, BCryptPasswordEncoder encoder) {
        this.chatService = chatService;
        this.encoder = encoder;
    }

    @PostMapping("/sign-up")
    public void signUp(@RequestBody Person person) {
        person.setPassword(encoder.encode(person.getPassword()));
        person.setRole(chatService.findRoleByName("ROLE_USER"));
        try {
            chatService.save(person);
        } catch (Exception e) {
            throw new IllegalArgumentException("Person with username "
                    + person.getUsername() + " exists");
        }
    }

    @GetMapping("/all")
    public List<Person> findAll() {
        return chatService.findAll();
    }
}
