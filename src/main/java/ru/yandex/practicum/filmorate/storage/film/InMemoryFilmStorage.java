package ru.yandex.practicum.filmorate.storage.film;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.NotExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import java.util.ArrayList;
import java.util.List;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final static Logger log = LoggerFactory.getLogger(FilmController.class);
    private final FilmValidator filmValidator;
    private final List<Film> films = new ArrayList<>();
    private int generator = 1;

    @Autowired
    public InMemoryFilmStorage(FilmValidator filmValidator) {
        this.filmValidator = filmValidator;
    }

    @Override
    public List<Film> findAll() {
        log.info("Get all films request");
        return films;
    }

    @Override
    public Film update(Film film) {

        boolean isExist = false;

        for (Film currentFilm : films) {
            if (currentFilm.getId() == film.getId()) {
                films.remove(currentFilm);
                isExist = true;
                break;
            }
        }
        log.info("Update film request");
        try {
            filmValidator.validate(film);

            if (isExist){
                films.add(film);
                return film;
            } else {
                log.error("Film does not exist in db");
                throw new NotExistException("Film not found");
            }
        } catch (ValidationException e){
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public Film findById(int id) {

        log.info("Get film by id " + id + "request");

        if (films.size()<1) {
            log.error("Film list is empty");
            throw new NotExistException("Film list is empty");
        }

        for (Film currentFilm : films) {
            if (currentFilm.getId() == id) {
                return currentFilm;
            }
        }
        log.error("Film not found");
        throw new NotExistException("Film not found");
    }

    @Override
    public Film create(Film film) {
        log.info("Create film request");
        try {
            filmValidator.validate(film);
            film.setId(generator);
            films.add(film);
            generator += 1;
            return film;
        } catch (ValidationException e){
            log.error(e.getMessage());
            throw e;
        }
    }
}
