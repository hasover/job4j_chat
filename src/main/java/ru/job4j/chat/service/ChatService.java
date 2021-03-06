package ru.job4j.chat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.chat.exception.DuplicateUserException;
import ru.job4j.chat.model.Message;
import ru.job4j.chat.model.Person;
import ru.job4j.chat.model.Role;
import ru.job4j.chat.model.Room;
import ru.job4j.chat.repository.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ChatService {
    private RoomRepository roomRepository;
    private MessageRepository messageRepository;
    private PersonRepository personRepository;
    private RoleRepository roleRepository;

    public ChatService(RoomRepository roomRepository, MessageRepository messageRepository,
                       PersonRepository personRepository, RoleRepository roleRepository) {
        this.roomRepository = roomRepository;
        this.messageRepository = messageRepository;
        this.personRepository = personRepository;
        this.roleRepository = roleRepository;
    }

    public List<Room> findAllRooms() {
        return StreamSupport.stream(roomRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public Room save(Room room) {
        return roomRepository.save(room);
    }

    public void deleteRoom(Room room) {
        roomRepository.delete(room);
    }

    public List<Message> findAllMessages() {
        return StreamSupport.stream(messageRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public Optional<Message> findMessage(int id) {
        return messageRepository.findById(id);
    }

    public List<Message> findRoomMessages(int roomId) {
        return messageRepository.findAllByRoomId(roomId);
    }

    public Message  createMessage(Message message) {
        Optional<Room> room = roomRepository.findById(message.getRoom().getId());
        if (room.isEmpty()) {
            throw new IllegalArgumentException("Room does not exist");
        }
        message.setRoom(room.get());
        return messageRepository.save(message);
    }

    public Message save(Message message) {
        return messageRepository.save(message);
    }

    public void deleteMessage(Message message) {
        messageRepository.delete(message);
    }

    public Person save(Person person) {
        try {
             return personRepository.save(person);
        } catch (Exception e) {
            throw new DuplicateUserException("Person with username " + person.getUsername()
                    + " already exists.");
        }
    }

    public List<Person> findAll() {
        return StreamSupport.stream(personRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public Person findPersonByUsername(String username) {
        return personRepository.findByUsername(username);
    }

    public Optional<Person> findPersonById(int id) {
        return personRepository.findById(id);
    }

    public Role findRoleByName(String name) {
        return roleRepository.findByName(name);
    }

    public Optional<Room> findRoomById(int roomId) {
        return roomRepository.findById(roomId);
    }
}
