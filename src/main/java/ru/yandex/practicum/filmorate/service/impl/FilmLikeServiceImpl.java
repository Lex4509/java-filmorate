package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmLikeDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmLikeService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmLikeServiceImpl implements FilmLikeService {

    private final FilmLikeDao filmLikeDao;

    @Override
    public List<Film> getMostLikedFilms(Integer count, Integer year, Long genreId) {
        if (year == null && genreId == null) {
            return filmLikeDao.findMostLikedFilms(count);
        }
        if (year != null && genreId != null) {
            return filmLikeDao.findMostLikedFilmsByYearAndGenre(count, year, genreId);
        }
        if (genreId != null) {
            return filmLikeDao.findMostLikedFilmsByGenre(count, genreId);
        }
        return filmLikeDao.findMostLikedFilmsByYear(count, year);
    }

    @Override
    public List<Long> getCommonFilmsIdByLikes(Long id) {
        return filmLikeDao.findCommonFilmsIdByLikes(id);
    }

    @Override
    public void like(Long filmId, Long userId) {
        filmLikeDao.add(filmId, userId);
    }

    @Override
    public void dislike(Long filmId, Long userId) {
        filmLikeDao.delete(filmId, userId);
    }
}
