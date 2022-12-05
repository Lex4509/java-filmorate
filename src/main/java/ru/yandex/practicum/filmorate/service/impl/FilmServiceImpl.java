package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.DirectorDao;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.exception.InvalidStatementException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.service.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.util.ExceptionThrowHandler.*;

@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {

    private final FilmDao filmDao;
    private final DirectorDao directorDao;
    private final FilmLikeService filmLikeService;
    private final UserServiceImpl userService;
    private final FilmGenreService filmGenreService;
    private final GenreService genreService;
    private final DirectorService directorService;
    private final FilmDirectorService filmDirectorService;

    @Override
    public List<Film> getAll() {
        return filmDao.findAll().stream()
                .map(this::setGenres)
                .map(this::setDirectors)
                .collect(Collectors.toList());
    }

    @Override
    public Film getById(final Long id) {
        final var film = filmDao.findById(id);
        throwExceptionIfFilmNotExists(film);

        setGenres(film);
        setDirectors(film);

        return film;
    }

    @Override
    public List<Film> getMostLikedFilms(Integer count) {
        List<Long> filmsId = filmLikeService.getMostLikedFilms(count);
        List<Film> films;

        if (filmsId.isEmpty()) {
            films = filmDao.findAllWithLimit(count);
        } else {
            films = filmDao.findByIds(filmsId);
        }

        return films.stream()
                .map(this::setGenres)
                .map(this::setDirectors)
                .collect(Collectors.toList());
    }

    @Override
    public Film save(final Film film) {
        final var filmForCheck = filmDao.findById(film.getId());
        throwExceptionIfFilmAlreadyExists(filmForCheck);
        final var createdFilm = filmDao.save(film);
        addGenres(film);
        addDirectors(film);

        return createdFilm;
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        final var film = getById(filmId);
        final var user = userService.getById(userId);
        throwExceptionIfFilmNotExists(film);
        throwExceptionIfUserNotExists(user);

        filmLikeService.like(filmId, userId);
    }

    @Override
    public Film update(final Film film) {
        final var filmForCheck = filmDao.findById(film.getId());
        throwExceptionIfFilmNotExists(filmForCheck);

        filmGenreService.deleteGenresFromFilm(film.getId());
        addGenres(film);
        setGenres(film);
        filmDirectorService.deleteDirectorsFromFilm(film.getId());
        addDirectors(film);
        setDirectors(film);
        return filmDao.update(film);
    }

    @Override
    public void deleteById(final Long id) {
        filmDao.deleteById(id);
    }

    @Override
    public void deleteLike(Long filmId, Long userId) {
        final var film = getById(filmId);
        final var user = userService.getById(userId);
        throwExceptionIfFilmNotExists(film);
        throwExceptionIfUserNotExists(user);

        filmLikeService.dislike(filmId, userId);
    }

    @Override
    public List<Film> getDirectorFilmsSorted(long directorId, String sortBy) {
        if (directorDao.findById(directorId)!=null) {
            if (sortBy.equals("year"))
                return filmDao.getDirectorFilmsSortedByYear(directorId).stream()
                        .map(this::setGenres)
                        .map(this::setDirectors)
                        .collect(Collectors.toList());
            else if (sortBy.equals("likes"))
                return filmDao.getDirectorFilmsSortedByLike(directorId).stream()
                        .map((this::setGenres))
                        .map(this::setDirectors)
                        .collect(Collectors.toList());
            else throw new InvalidStatementException("Invalid sort parameter");
        }
        else throw new NotFoundException("Director does not exist");
    }

    private Film setGenres(Film film) {
        List<Long> genreIds = filmGenreService.getByFilmId(film.getId())
                .stream()
                .map(FilmGenre::getGenreId)
                .collect(Collectors.toList());

        List<Genre> genres = genreService.getByIds(genreIds);
        film.setGenres(genres);
        return film;
    }

    private void addGenres(Film film) {
        if (film.getGenres() == null) return;

        final Set<Long> genresId = film.getGenres().stream()
                .map(Genre::getId)
                .collect(Collectors.toSet());

        filmGenreService.addGenresToFilm(film.getId(), genresId);
    }

    private Film setDirectors(Film film) {
        List<Long> directorIds = filmDirectorService.getByFilmId(film.getId())
                .stream()
                .map(FilmDirector::getDirectorId)
                .collect(Collectors.toList());

        List<Director> directors = directorService.getByIds(directorIds);
        film.setDirectors(directors);
        return film;
    }

    private void addDirectors(Film film) {
        if (film.getDirectors() == null) return;

        final Set<Long> directorsId = film.getDirectors().stream()
                .map(Director::getId)
                .collect(Collectors.toSet());

        filmDirectorService.addDirectorsToFilm(film.getId(), directorsId);
    }
}