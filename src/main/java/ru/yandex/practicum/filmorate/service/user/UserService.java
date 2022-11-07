package ru.yandex.practicum.filmorate.service.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ExistException;
import ru.yandex.practicum.filmorate.exception.NotExistException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserStorage userStorage;

    private final static Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addFriend(int id, int friendId){
        log.info("Add friend " + friendId + " by user " + id);
        User user = userStorage.findById(id);
        User friend = userStorage.findById(friendId);

        if (user.getFriends().add(friend.getId())) {
            friend.getFriends().add(user.getId());
            return user;
        }
        else {
            log.error("Friend is already exist");
            throw new ExistException("Friend is already exist");
        }
    }

    public User deleteFriend(int id, int friendId){
        log.info("Delete friend " + friendId + " by user " + id);
        User user = userStorage.findById(id);
        User friend = userStorage.findById(friendId);

        if (user.getFriends().remove(friend.getId())) {
            friend.getFriends().remove(user.getId());
            return user;
        }
        else {
            log.error("Friend not found");
            throw new NotExistException("Friend not found");
        }
    }

    public List<User> getFriends(int id){
        log.info("Get friends request by id " + id);
        User user = userStorage.findById(id);
        List<User> friends = new ArrayList<>();
        for (Integer friendId : user.getFriends()) {
            friends.add(userStorage.findById(friendId));
        }
        return friends;
    }

    public List<User> getMutualFriends(int id, int otherId){
        log.info("Get mutual friends for ids " + id + " and " + otherId + " request");
        User user = userStorage.findById(id);
        User otherUser = userStorage.findById(otherId);

        List<User> mutualFriends = new ArrayList<>();
        List<Integer> mutualFriendIds = user.getFriends().stream()
                .filter(otherUser.getFriends()::contains)
                .collect(Collectors.toList());
        for (Integer mutualFriendId : mutualFriendIds) {
            mutualFriends.add(userStorage.findById(mutualFriendId));
        }
        return mutualFriends;
    }

}
