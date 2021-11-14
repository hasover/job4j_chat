package ru.job4j.chat.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.chat.model.Message;
import ru.job4j.chat.model.Person;
import ru.job4j.chat.model.Room;
import ru.job4j.chat.repository.MessageRepository;
import ru.job4j.chat.repository.PersonRepository;
import ru.job4j.chat.repository.RoomRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ChatService {
    private RoomRepository roomRepository;
    private MessageRepository messageRepository;
    private PersonRepository personRepository;

    public ChatService(RoomRepository roomRepository, MessageRepository messageRepository,
                       PersonRepository personRepository) {
        this.roomRepository = roomRepository;
        this.messageRepository = messageRepository;
        this.personRepository = personRepository;
    }

    public List<Room> findAllRooms() {
        return StreamSupport.stream(roomRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public Room createRoom(Room room) {
        return roomRepository.save(room);
    }

    @Transactional
    public void editRoom(Room room) {
        Optional<Room> roomFromStore = roomRepository.findById(room.getId());
        roomFromStore.ifPresent(value -> value.setName(room.getName()));
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
        return messageRepository.findAllByRoom_Id(roomId);
    }

    public Message  createMessage(Message message) {
        Optional<Room> room = roomRepository.findById(message.getRoom().getId());
        if (room.isEmpty()) {
            throw new IllegalArgumentException("Room does not exist");
        }
        message.setRoom(room.get());
        return messageRepository.save(message);
    }

    @Transactional
    public void editMassage(Message message) {
        Optional<Message> messageFromStore = messageRepository.findById(message.getId());
        messageFromStore.ifPresent(value -> value.setName(message.getName()));
    }

    public void deleteMessage(Message message) {
        messageRepository.delete(message);
    }

    public Person verifyPerson(Person person) {
        Person validPerson = personRepository.findByUsername(person.getUsername());
        if (validPerson == null || !validPerson.getPassword().equals(person.getPassword())) {
            throw new IllegalArgumentException("Invalid user");
        }
        return validPerson;
    }

}
