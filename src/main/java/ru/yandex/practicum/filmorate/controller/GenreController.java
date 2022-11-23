package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenresDao;

import java.util.List;
import java.util.Map;

@RestController
public class GenreController {

    private final GenresDao genresDao;

    @Autowired
    public GenreController(GenresDao genresDao) {
        this.genresDao = genresDao;
    }

    @GetMapping(value = "/genres")
    public List<Genre> findAll(){
        return genresDao.findAll();
    }

    @GetMapping(value = "/genres/{id}")
    public Genre findById(@PathVariable int id){
        return genresDao.findById(id);
    }

}
