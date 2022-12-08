package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmDao {

    List<Film> findAll();

    List<Film> findByIds(List<Long> filmsId);

    Film findById(Long id);

    Film save(Film film);

    Film update(Film film);

    void deleteById(Long id);


    List<Film> getCommonFilms(Long userId, Long friendId);
    List<Film> getDirectorFilmsSortedByYear(long directorId);
    List<Film> getDirectorFilmsSortedByLike(long directorId);
    List<Film> searchByTitle(String query);
    List<Film> searchByDirector(String query);
    List<Film> searchByTitleAndDirector(String query);
}
