package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.FilmDirector;

import java.util.List;

public interface FilmDirectorDao {

    List<FilmDirector> findByFilmId(Long filmId);

    void add(List<FilmDirector> filmDirectors);

    void delete(Long filmId);

}
