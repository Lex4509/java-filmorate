package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmLikeService {

    List<Film> getMostLikedFilms(Integer count, Integer year, Long genreId);

    List<Long> getCommonFilmsIdByLikes(Long id);

    void like(Long filmId, Long userId);

    void dislike(Long filmId, Long userId);


}
