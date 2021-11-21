package ru.job4j.chat.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

public class RoomDTO {
    @Min(value = 1, message = "id must be greater than 0")
    private int id;
    @NotBlank(message = "name must be greater than 0")
    private String name;
    @Min(value = 1, message = "id must be greater than 0")
    private int personId;

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
