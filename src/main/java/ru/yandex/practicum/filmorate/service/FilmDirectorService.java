package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.FilmDirector;

import java.util.List;
import java.util.Set;

public interface FilmDirectorService {

    List<FilmDirector> getByFilmId(Long filmId);

    void addDirectorsToFilm(Long filmId, Set<Long> directorsId);

    void deleteDirectorsFromFilm(Long filmId);

}
