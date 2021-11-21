package ru.job4j.chat.model;

import javax.validation.constraints.NotBlank;

public class PersonDTO {
    @NotBlank(message = "username must not be blank")
    private String username;
    @NotBlank(message = "name must not be blank")
    private String name;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
