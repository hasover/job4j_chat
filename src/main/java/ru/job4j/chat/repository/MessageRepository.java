package ru.job4j.chat.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.job4j.chat.model.Message;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends CrudRepository<Message, Integer> {

    @Query("from Message m join fetch m.room join fetch m.person where m.person.id =:param")
    List<Message> findAllByRoomId(@Param("param") int id);

    @Query("from Message m join fetch m.room join fetch m.person")
    Iterable<Message> findAll();

    @Query("from Message m join fetch m.room join fetch m.person where m.id =:param")
    Optional<Message> findById(@Param("param") int id);

}
