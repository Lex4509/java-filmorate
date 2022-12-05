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

    private final FilmService FilmService;
    private final FilmStorage filmStorage;

    @Autowired
    public FilmController(@Qualifier("filmDbService") FilmService FilmService, @Qualifier("filmDbStorage") FilmStorage filmStorage) {
        this.FilmService = FilmService;
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
        return FilmService.like(id, userId);
    }

    @DeleteMapping(value = "/films/{id}/like/{userId}")
    public Film disLike(@PathVariable int id, @PathVariable int userId) {
        return FilmService.disLike(id, userId);
    }

    @GetMapping(value = "/films/popular")
    public List<Film> showMostPopularFilms(@RequestParam(required = false, defaultValue = "10") int count){
        return FilmService.showMostPopularFilms(count);
    }

}
