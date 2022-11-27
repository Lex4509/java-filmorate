package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ExistException;
import ru.yandex.practicum.filmorate.exception.NotExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    private final UserStorage userStorage;
    private final UserService UserService;

    @Autowired
    public UserController(@Qualifier("userDbStorage") UserStorage userStorage, @Qualifier("userDbService") UserService UserService) {
        this.userStorage = userStorage;
        this.UserService = UserService;
    }

    @GetMapping("/users")
    public List<User> findAll(){
        return userStorage.findAll();
    }

    @PostMapping(value = "/users")
    public User create(@RequestBody User user){
        return userStorage.create(user);
    }

    @PutMapping(value = "/users")
    public User update (@RequestBody User user) throws NotExistException, ValidationException {
        return userStorage.update(user);
    }

    @GetMapping(value = "/users/{id}")
    public User findById(@PathVariable int id){
        return userStorage.findById(id);
    }

    @PutMapping(value = "/users/{id}/friends/{friendId}")
    public User addFriend(@PathVariable int id, @PathVariable int friendId){
        return UserService.addFriend(id, friendId);
    }

    @DeleteMapping(value = "/users/{id}/friends/{friendId}")
    public User deleteFriend(@PathVariable int id, @PathVariable int friendId){
        return UserService.deleteFriend(id, friendId);
    }

    @GetMapping(value = "/users/{id}/friends")
    public List<User> getFriends(@PathVariable int id){
        return UserService.getFriends(id);
    }

    @GetMapping(value = "/users/{id}/friends/common/{otherId}")
    public List<User> getMutualFriends(@PathVariable int id, @PathVariable int otherId){
        return UserService.getMutualFriends(id, otherId);
    }
}
