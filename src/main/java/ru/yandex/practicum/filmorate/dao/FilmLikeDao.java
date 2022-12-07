package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmLike;

import java.util.List;

public interface FilmLikeDao {

    List<FilmLike> findAllFilmLike();

    List<Film> findMostLikedFilms(int limit);

    List<Film> findMostLikedFilmsByYear(Integer count, Integer year);

    List<Film> findMostLikedFilmsByGenre(Integer count, Long genreId);

    List<Film> findMostLikedFilmsByYearAndGenre(Integer count, Integer year, Long genreId);

    List<Long> findCommonFilmsIdByLikes(Long id);

    void add(Long filmId, Long userId);

    void delete(Long filmId, Long userId);
}
