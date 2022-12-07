package ru.yandex.practicum.filmorate.service;

import org.springframework.web.bind.annotation.PathVariable;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.event.Event;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface UserService {

    List<User> getAll();

    User getById(Long id);

    List<User> getMutualFriends(Long id, Long otherId);

    List<User> getUserFriends(Long id);

    User save(User user);

    void addFriend(Long userId, Long friendId);

    User update(User user);

    void deleteById(Long id);

    void deleteFriend(Long userId, Long friendId);

    List<Event> getFeed(@PathVariable @NotNull Long id);
}
