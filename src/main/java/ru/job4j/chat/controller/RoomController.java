package ru.job4j.chat.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.job4j.chat.model.Message;
import ru.job4j.chat.model.Person;
import ru.job4j.chat.model.Room;
import ru.job4j.chat.service.ChatService;
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
        HttpStatus status = messages.isEmpty() ? HttpStatus.NOT_FOUND : HttpStatus.OK;
        return new ResponseEntity<>(messages, status);
    }

    @PostMapping("/")
    public ResponseEntity<Room> createNewRoom(@RequestBody Room room) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        room.setPerson(chatService.findPersonByUsername(username));
        return new ResponseEntity<>(chatService.createRoom(room), HttpStatus.CREATED);
    }

    @PutMapping("/")
    public ResponseEntity<Void> update(@RequestBody Room room) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        room.setPerson(chatService.findPersonByUsername(username));
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
