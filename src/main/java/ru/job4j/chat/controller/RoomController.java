package ru.job4j.chat.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.chat.model.Message;
import ru.job4j.chat.model.Person;
import ru.job4j.chat.model.Room;
import ru.job4j.chat.service.ChatService;

import java.util.Collections;
import java.util.List;

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
            return new ResponseEntity<>(Collections.EMPTY_LIST, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(messages, HttpStatus.OK);
        }
    }

    @PostMapping("/")
    public ResponseEntity<Room> createNewRoom(@RequestBody Room room) {
        Person verified = chatService.verifyPerson(room.getPerson());
        room.setPerson(verified);
        return new ResponseEntity<>(chatService.createRoom(room), HttpStatus.CREATED);
    }

    @PutMapping("/")
    public ResponseEntity<Void> update(@RequestBody Room room) {
        Person verified = chatService.verifyPerson(room.getPerson());
        room.setPerson(verified);
        chatService.editRoom(room);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        Room room = new Room();
        room.setId(id);
        chatService.deleteRoom(room);
        return ResponseEntity.ok().build();
    }
}
