package ru.job4j.chat.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.chat.model.Message;
import ru.job4j.chat.model.Person;
import ru.job4j.chat.model.Room;
import ru.job4j.chat.model.RoomDTO;
import ru.job4j.chat.service.ChatService;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/room")
public class RoomController {
    private ChatService chatService;

    public RoomController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/")
    public List<Room> findAll() {
        return chatService.findAllRooms();
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<Message>> findRoomMessages(@PathVariable int id) {
        List<Message> messages = chatService.findRoomMessages(id);
        if (messages.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "No messages found for room id= " + id);
        }
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<Room> createNewRoom(@Valid @RequestBody Room room) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        room.setPerson(chatService.findPersonByUsername(username));
        return new ResponseEntity<>(chatService.save(room), HttpStatus.CREATED);
    }

    @PutMapping("/")
    public ResponseEntity<Void> update(@Valid @RequestBody RoomDTO roomDTO) {
        Room room = chatService.findRoomById(roomDTO.getId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Room not found"));
        Person person = chatService.findPersonById(roomDTO.getPersonId())
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Room not found"));

        room.setPerson(person);
        room.setName(roomDTO.getName());
        room.getMessages().clear();
        chatService.save(room);
        return ResponseEntity.ok().build();
    }

    @PatchMapping
    public Room edit(@RequestBody Map<String, String> map) {
        String id = map.get("id");
        String name = map.get("name");
        if (id == null || name == null) {
            throw new NullPointerException("Fields id and name must not be empty");
        }
        Room room = chatService.findRoomById(Integer.parseInt(id))
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Room not found"));
        room.setName(name);
        return chatService.save(room);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        Room room = new Room();
        room.setId(id);
        chatService.deleteRoom(room);
        return ResponseEntity.ok().build();
    }
}
