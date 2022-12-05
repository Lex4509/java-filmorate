package ru.yandex.practicum.filmorate.storage.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.NotExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.util.ArrayList;
import java.util.List;

@Component
public class InMemoryUserStorage implements UserStorage{

    private final static Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserValidator userValidator;
    private final List<User> users = new ArrayList<>();
    private int generator = 1;

    @Autowired
    public InMemoryUserStorage(UserValidator userValidator) {
        this.userValidator = userValidator;
    }

    @Override
    public List<User> findAll() {
        log.info("Find all users request");
        return users;
    }

    @Override
    public User create(User user) {
        log.info("Create user request");
        try {
            userValidator.validate(user);
            user.setId(generator);
            if (user.getName() == "") {
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

    @Override
    public User update(User user) {
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
                log.error("User not found");
                throw new NotExistException("User not found");
            }
        } catch (ValidationException e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public User findById(int id) {
        log.info("Find user by id " + id + " request");
        if (users.size()<1) {
            log.error("User list is empty");
            throw new NotExistException("User list is empty");
        }

        for (User currentUser : users) {
            if (currentUser.getId() == id) {
                return currentUser;
            }
        }
        log.error("User not found");
        throw new NotExistException("User not found");
    }

}
