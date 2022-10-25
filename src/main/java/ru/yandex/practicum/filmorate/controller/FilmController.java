package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ExistionException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
public class FilmController {

    private final static Logger log = LoggerFactory.getLogger(FilmController.class);

    private final FilmValidator filmValidator = new FilmValidator();
    private final List<Film> films = new ArrayList<>();
    private int generator = 1;

    @GetMapping(value = "/films")
    public List<Film> findAll(){
        return films;
    }

    @PostMapping(value = "/films")
    public Film create(@Valid @RequestBody Film film) throws ValidationException {
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

    @PutMapping(value = "/films")
    public Film update(@Valid @RequestBody Film film) throws ExistionException, ValidationException {
        log.info("Update film request");
        try {
            filmValidator.validate(film);
            boolean isExist = false;
            for (Film currentFilm : films) {
                if (currentFilm.getId() == film.getId()) {
                    films.remove(currentFilm);
                    isExist = true;
                    break;
                }
            }
            if (isExist){
                films.add(film);
                return film;
            } else {
                log.error("Film does not exist in db");
                throw new ExistionException("Film does not exist in db");
            }
        } catch (ValidationException e){
            log.error(e.getMessage());
            throw e;
        }
    }

}
