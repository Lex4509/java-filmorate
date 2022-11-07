package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ExistException;
import ru.yandex.practicum.filmorate.exception.NotExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.validation.Valid;
import java.util.*;

@RestController
public class UserController {

    private final UserStorage userStorage;
    private final UserService userService;

    @Autowired
    public UserController(UserStorage userStorage, UserService userService) {
        this.userStorage = userStorage;
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<User> findAll(){
        return userStorage.findAll();
    }

    @PostMapping(value = "/users")
    public User create(@Valid @RequestBody User user){
        return userStorage.create(user);
    }

    @PutMapping(value = "/users")
    public User update (@Valid @RequestBody User user) throws NotExistException, ValidationException {
        return userStorage.update(user);
    }

    @GetMapping(value = "/users/{id}")
    public User findById(@PathVariable int id){
        return userStorage.findById(id);
    }

    @PutMapping(value = "/users/{id}/friends/{friendId}")
    public User addFriend(@PathVariable int id, @PathVariable int friendId){
        return userService.addFriend(id, friendId);
    }

    @DeleteMapping(value = "/users/{id}/friends/{friendId}")
    public User deleteFriend(@PathVariable int id, @PathVariable int friendId){
        return userService.deleteFriend(id, friendId);
    }

    @GetMapping(value = "/users/{id}/friends")
    public List<User> getFriends(@PathVariable int id){
        return userService.getFriends(id);
    }

    @GetMapping(value = "/users/{id}/friends/common/{otherId}")
    public List<User> getMutualFriends(@PathVariable int id, @PathVariable int otherId){
        return userService.getMutualFriends(id, otherId);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationException(final ValidationException e){
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotExistException(final NotExistException e){
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleExistException(final ExistException e){
        return Map.of("error", e.getMessage());
    }

}
