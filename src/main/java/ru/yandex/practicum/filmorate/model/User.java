package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Data
public class User {

    private int id;
    @Email(message = "Invalid e-mail")
    private final String email;
    @NotBlank(message = "Invalid login")
    private final String login;
    private String name;
    private final LocalDate birthday;

    public User(String email, String login, String name, LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }
}
