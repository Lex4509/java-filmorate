package ru.yandex.practicum.filmorate.service.film;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ExistException;
import ru.yandex.practicum.filmorate.exception.NotExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    private final static Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public FilmService(FilmStorage filmStorage, @Qualifier("inMemoryUserStorage") UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film like(int id, int userId){
        log.info("Like film " + id + " by user " + userId + " request");
        Film film = filmStorage.findById(id);
        User user = userStorage.findById(userId);

        if (film.getLikeUsers().add(user.getId()))
            return film;
        else {
            log.error("Like is already exist");
            throw new ExistException("Like is already exist");
        }
    }

    public Film disLike(int id, int userId){
        log.info("Dislike film " + id + " by user " + userId + " request");
        Film film = filmStorage.findById(id);
        User user = userStorage.findById(userId);

        if (film.getLikeUsers().remove(user.getId()))
            return film;
        else {
            log.error("Like not found");
            throw new NotExistException("Like not found");
        }
    }

    public List<Film> showMostPopularFilms(int count){
        log.info("Most popular " + count + " films request");
        return filmStorage.findAll().stream().sorted((o1, o2) -> o2.getLikeUsers().size() - o1.getLikeUsers().size())
                .limit(count)
                .collect(Collectors.toList());
    }
}
