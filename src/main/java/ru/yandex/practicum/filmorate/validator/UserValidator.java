package ru.yandex.practicum.filmorate.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public class UserValidator {

    private final static Logger log = LoggerFactory.getLogger(UserValidator.class);

    public void validate(User user) throws ValidationException {
        if (user.getLogin().equals("")) {
            log.error("Login length should not be empty");
            throw new ValidationException("Invalid login");
        }
        if (user.getLogin().contains(" ")){
            log.error("Login may not contain spaces");
            throw new ValidationException("Invalid login");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Birthday should not be in future");
            throw new ValidationException("Invalid birthday");
        }
    }

}
