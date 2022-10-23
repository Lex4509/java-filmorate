package ru.yandex.practicum.filmorate.controllerTest;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserControllerTest {

    UserController userController = new UserController();

    @Test
    void validateUserOk() throws ValidationException {
        userController.validate(new User("mail@mail.ru", "login", "Name", LocalDate.parse("2000-01-01")));
    }

    @Test
    void validateUserEmptyLogin(){
        Exception exception = assertThrows(ValidationException.class,
                () -> userController.validate(new User("mail@mail.ru", "", "Name", LocalDate.parse("2000-01-01"))));
        assertEquals("Invalid login", exception.getMessage());
    }

    @Test
    void validateUserFailLogin(){
        Exception exception = assertThrows(ValidationException.class,
                () -> userController.validate(new User("mail@mail.ru", "lo gin", "Name", LocalDate.parse("2000-01-01"))));
        assertEquals("Invalid login", exception.getMessage());
    }

    @Test
    void validateUserFailBirthday(){
        Exception exception = assertThrows(ValidationException.class,
                () -> userController.validate(new User("mail@mail.ru", "login", "Name", LocalDate.parse("3000-01-01"))));
        assertEquals("Invalid birthday", exception.getMessage());
    }
}
