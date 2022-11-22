package ru.yandex.practicum.filmorate.service.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ExistException;
import ru.yandex.practicum.filmorate.exception.NotExistException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.util.List;

@Component
public class UserDbService implements UserService{

    private final static Logger log = LoggerFactory.getLogger(UserDbService.class);

    private final JdbcTemplate jdbcTemplate;
    private final UserDbStorage userDbStorage;
    private final int DEFAULT_FRIENDSHIP_STATUS = 1;

    @Autowired
    public UserDbService(JdbcTemplate jdbcTemplate, UserDbStorage userDbStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.userDbStorage = userDbStorage;
    }

    @Override
    public User addFriend(int id, int friendId) {

        log.info("Add friend " + friendId + " by user " + id);

        User user = userDbStorage.findById(id);
        User friend = userDbStorage.findById(friendId);

        String sql = "INSERT INTO friendship (user_id, friend_id, friendship_status_id) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, id, friendId, DEFAULT_FRIENDSHIP_STATUS);

        return user;
    }

    @Override
    public User deleteFriend(int id, int friendId) {

        String sql = "DELETE FROM friendship WHERE user_id = ? AND friend_id = ?";
        int result = jdbcTemplate.update(sql, id, friendId);

        if (result == 0){
            log.error("Friendship not found");
            throw new NotExistException("Friendship not found");
        }

        return userDbStorage.findById(id);
    }

    @Override
    public List<User> getFriends(int id) {

        log.info("Get friends request by id " + id);

        userDbStorage.findById(id);

        String sql = "SELECT * FROM usr WHERE id in (SELECT friend_id FROM friendship WHERE user_id = ?)";
        return jdbcTemplate.query(sql, new UserMapper(), id);
    }

    @Override
    public List<User> getMutualFriends(int id, int otherId) {

        log.info("Get mutual friends for ids " + id + " and " + otherId + " request");

        userDbStorage.findById(id);
        userDbStorage.findById(otherId);

        String sql = "SELECT * FROM usr WHERE id in (SELECT friend_id FROM friendship WHERE user_id = ?) AND id in " +
                "(SELECT friend_id FROM friendship WHERE user_id = ?)";

        return jdbcTemplate.query(sql, new UserMapper(), id, otherId);
    }
}
