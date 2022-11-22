package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotExistException;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDao;

import java.util.List;
import java.util.Map;

@RestController
public class MpaController {

    private final MpaDao mpaDao;

    @Autowired
    public MpaController(MpaDao mpaDao) {
        this.mpaDao = mpaDao;
    }

    @GetMapping(value = "/mpa")
    public List<MPA> findAll(){
        return mpaDao.findAll();
    }

    @GetMapping(value = "/mpa/{id}")
    public MPA findById(@PathVariable int id){
        return mpaDao.findById(id);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotExistException(final NotExistException e){
        return Map.of("error", e.getMessage());
    }

}
