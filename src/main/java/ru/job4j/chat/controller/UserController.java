package ru.job4j.chat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.chat.exception.DuplicateUserException;
import ru.job4j.chat.model.Person;
import ru.job4j.chat.model.PersonDTO;
import ru.job4j.chat.service.ChatService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    private static final Logger LOGGER =
            LoggerFactory.getLogger(UserController.class.getSimpleName());
    private ObjectMapper objectMapper;
    private ChatService chatService;
    private BCryptPasswordEncoder encoder;

    public UserController(ObjectMapper objectMapper, ChatService chatService,
                          BCryptPasswordEncoder encoder) {
        this.objectMapper = objectMapper;
        this.chatService = chatService;
        this.encoder = encoder;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<Person> signUp(@Valid @RequestBody Person person) {
        person.setPassword(encoder.encode(person.getPassword()));
        person.setRole(chatService.findRoleByName("ROLE_USER"));
        chatService.save(person);
        return new ResponseEntity<>(person, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Person>> findAll() {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(chatService.findAll());
    }

    @PatchMapping("/")
    public Person editUser(@Valid@RequestBody PersonDTO personDTO) {
        Person person = chatService.findPersonByUsername(personDTO.getUsername());
        if (person == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        person.setName(personDTO.getName());
        return chatService.save(person);
    }

    @ExceptionHandler(value = {DuplicateUserException.class})
    public void handleDuplicateUser(Exception e, HttpServletRequest request,
                                    HttpServletResponse response) {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType("application/json");
        try (PrintWriter writer = response.getWriter()) {
            writer.write(objectMapper.writeValueAsString(new HashMap<>() { {
                put("message", e.getMessage());
                put("type", "Duplicate user error");
            }}));
        } catch (IOException ex) {
            LOGGER.error(ex.getLocalizedMessage());
        }
        LOGGER.error(e.getLocalizedMessage());
    }
}
