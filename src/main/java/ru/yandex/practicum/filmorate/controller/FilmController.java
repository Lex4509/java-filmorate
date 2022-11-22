package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ExistException;
import ru.yandex.practicum.filmorate.exception.NotExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;
import java.util.Map;

@RestController
public class FilmController {

    private final FilmService filmService;
    private final FilmStorage filmStorage;

    @Autowired
    public FilmController(FilmService filmService, @Qualifier("filmDbStorage") FilmStorage filmStorage) {
        this.filmService = filmService;
        this.filmStorage = filmStorage;
    }

    @GetMapping(value = "/films")
    public List<Film> findAll(){
        return filmStorage.findAll();
    }

    @GetMapping(value = "/films/{id}")
    public Film findById(@PathVariable int id){
        return filmStorage.findById(id);
    }

    @PostMapping(value = "/films")
    public Film create(@RequestBody Film film) throws ValidationException {
        return filmStorage.create(film);
    }

    @PutMapping(value = "/films")
    public Film update(@RequestBody Film film) throws NotExistException, ValidationException {
        return filmStorage.update(film);
    }

    @PutMapping(value = "/films/{id}/like/{userId}")
    public Film like(@PathVariable int id, @PathVariable int userId) {
        return filmService.like(id, userId);
    }

    @DeleteMapping(value = "/films/{id}/like/{userId}")
    public Film disLike(@PathVariable int id, @PathVariable int userId) {
        return filmService.disLike(id, userId);
    }

    @GetMapping(value = "/films/popular")
    public List<Film> showMostPopularFilms(@RequestParam(required = false, defaultValue = "10") int count){
        return filmService.showMostPopularFilms(count);
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
