package ru.job4j.chat.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.chat.model.Message;
import ru.job4j.chat.model.MessageDTO;
import ru.job4j.chat.model.Person;
import ru.job4j.chat.model.Room;
import ru.job4j.chat.service.ChatService;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
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
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Message with id=" + id + " does not exist");
        }
        return new ResponseEntity<>(message.get(), HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<Message> createNewMessage(@Valid @RequestBody Message message) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Person person = chatService.findPersonByUsername(username);
        message.setPerson(person);
        return new ResponseEntity<>(chatService.createMessage(message), HttpStatus.CREATED);
    }

    @PutMapping("/")
    public ResponseEntity<Void> update(@Valid @RequestBody MessageDTO messageDTO) {
        Person person = chatService.findPersonById(messageDTO.getId())
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));
        Room room = chatService.findRoomById(messageDTO.getRoomId())
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "room not found"));
        Message message = new Message();
        message.setId(messageDTO.getId());
        message.setName(messageDTO.getName());
        message.setRoom(room);
        message.setPerson(person);
        chatService.save(message);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/")
    public Message edit(@RequestBody Map<String, String> map) {
        String id = map.get("id");
        String name = map.get("name");
        if (id == null || name == null) {
            throw new NullPointerException("Fields id and name must not be empty");
        }
        Message message = chatService.findMessage(Integer.parseInt(id))
                .orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Message not found"));
        message.setName(name);
        return chatService.save(message);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        Message message = new Message();
        message.setId(id);
        chatService.deleteMessage(message);
        return ResponseEntity.ok().build();
    }
}
