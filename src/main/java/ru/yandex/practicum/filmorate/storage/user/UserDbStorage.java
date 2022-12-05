package ru.yandex.practicum.filmorate.storage.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.NotExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.UserValidator;
import java.sql.PreparedStatement;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class UserDbStorage implements UserStorage{

    private final UserValidator userValidator;
    private final static Logger log = LoggerFactory.getLogger(UserController.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(UserValidator userValidator, JdbcTemplate jdbcTemplate) {
        this.userValidator = userValidator;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> findAll() {
        log.info("Find all users request");
        String sql = "SELECT * FROM usr";
        return jdbcTemplate.query(sql, new UserMapper());
    }

    @Override
    public User create(User user) {

        log.info("Create user request");

        try {
            userValidator.validate(user);
            String sql = "INSERT INTO usr (email, login, name, birthday) VALUES (?,?,?,?)";

            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                PreparedStatement prepareStatement = connection.prepareStatement(sql, new String[]{"id"});
                prepareStatement.setString(1, user.getEmail());
                prepareStatement.setString(2, user.getLogin());
                if (user.getName() == "" || user.getName() == null) {
                    prepareStatement.setString(3, user.getLogin());
                    user.setName(user.getLogin());
                }
                else prepareStatement.setString(3, user.getName());
                prepareStatement.setString(4, user.getBirthday().format(DateTimeFormatter.ISO_DATE));
                return prepareStatement;
            }, keyHolder);

            user.setId(keyHolder.getKey().intValue());
            return user;
        } catch (ValidationException e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public User update(User user) {

        log.info("Update user request");

        try {
            userValidator.validate(user);
            String sql = "UPDATE usr SET name = ?, login = ?, email = ?, birthday = ? WHERE id = ?";
            int result = jdbcTemplate.update(sql, user.getName(), user.getLogin(), user.getEmail(), user.getBirthday(), user.getId());

            if (result == 0) {
                log.error("User not found");
                throw new NotExistException("User not found");
            }

            log.info("User id " + user.getId() + " updated");

            return user;
        } catch (ValidationException e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public User findById(int id) {

        log.info("Find user by id " + id + " request");

        String sql = "SELECT * FROM usr WHERE id = ?";

        List<Object> result = jdbcTemplate.query(sql, new UserMapper(), id);

        if (result.size() == 0) {
            log.error("User not exist in db");
            throw new NotExistException("User not exist in db");
        }

        return (User) result.get(0);
    }
}
