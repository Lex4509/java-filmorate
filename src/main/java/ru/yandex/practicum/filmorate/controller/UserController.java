package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ExistionException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import javax.validation.Valid;
import java.util.*;

@RestController
public class UserController {

    private final static Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserValidator userValidator = new UserValidator();
    private final List<User> users = new ArrayList<>();
    private int generator = 1;

    @GetMapping("/users")
    public List<User> findAll(){
        return users;
    }

    @PostMapping(value = "/users")
    public User create(@Valid @RequestBody User user) throws ValidationException {
        log.info("Create user request");
        try {
            userValidator.validate(user);
            user.setId(generator);
            if (user.getName() == null) {
                user.setName(user.getLogin());
            }
            users.add(user);
            generator += 1;
            return user;
        } catch (ValidationException e){
            log.error(e.getMessage());
            throw e;
        }
    }

    @PutMapping(value = "/users")
    public User update (@Valid @RequestBody User user) throws ExistionException, ValidationException {
        log.info("Update user request");
        try {
            userValidator.validate(user);
            if (user.getName() == null) {
                user.setName(user.getLogin());
            }

            boolean isExist = false;
            for (User currentUser : users) {
                if (currentUser.getId() == user.getId()) {
                    users.remove(currentUser);
                    isExist = true;
                    break;
                }
            }
            if (isExist){
                users.add(user);
                return user;
            } else {
                log.error("User does not exist in db");
                throw new ExistionException("User does not exist in db");
            }
        } catch (ValidationException e) {
            log.error(e.getMessage());
            throw e;
        }
    }

}
