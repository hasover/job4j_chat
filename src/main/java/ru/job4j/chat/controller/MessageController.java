package ru.job4j.chat.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.job4j.chat.model.Message;
import ru.job4j.chat.model.Person;
import ru.job4j.chat.service.ChatService;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/message")
public class MessageController {
    private ChatService chatService;

    public MessageController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/")
    public List<Message> findAll() {
        return chatService.findAllMessages();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Message> findMessage(@PathVariable int id) {
        Optional<Message> message = chatService.findMessage(id);
        if (message.isEmpty()) {
            throw new IllegalArgumentException("Message with id=" + id + " does not exist");
        }
        return new ResponseEntity<>(message.get(), HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<Message> createNewMessage(@RequestBody Message message) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Person person = chatService.findPersonByUsername(username);
        message.setPerson(person);
        return new ResponseEntity<>(chatService.createMessage(message), HttpStatus.CREATED);
    }

    @PutMapping("/")
    public ResponseEntity<Void> update(@RequestBody Message message) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Person person = chatService.findPersonByUsername(username);
        message.setPerson(person);
        chatService.editMassage(message);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        Message message = new Message();
        message.setId(id);
        chatService.deleteMessage(message);
        return ResponseEntity.ok().build();
    }
}
