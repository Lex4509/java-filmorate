package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.EventDao;
import ru.yandex.practicum.filmorate.dao.FilmLikeDao;
import ru.yandex.practicum.filmorate.mapper.FilmLikeMapper;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmLike;
import ru.yandex.practicum.filmorate.model.event.EventType;
import ru.yandex.practicum.filmorate.model.event.Operation;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FilmLikeDaoImpl implements FilmLikeDao {

    private final JdbcTemplate jdbcTemplate;
    private final EventDao eventDao;

    @Override
    public List<FilmLike> findAllFilmLike() {
        final var sql = "SELECT * " +
                "FROM film_like";
        return jdbcTemplate.query(sql, new FilmLikeMapper());
    }

    @Override
    public List<Film> findMostLikedFilms(int limit) {
        final var sql = "SELECT f.*, m.* " +
                "FROM film_like AS fl " +
                "RIGHT JOIN film AS f ON fl.film_id = f.film_id " +
                "LEFT JOIN mpa AS m ON f.mpa_id = m.mpa_id " +
                "GROUP BY f.film_id " +
                "ORDER BY COUNT(fl.user_id) DESC " +
                "LIMIT ?";
        return jdbcTemplate.query(sql, new FilmMapper(), limit);
    }

    @Override
    public List<Film> findMostLikedFilmsByYear(Integer count, Integer year) {
        final var sql = "SELECT f.*, m.* " +
                "FROM film_like AS fl " +
                "RIGHT JOIN film AS f ON fl.film_id = f.film_id " +
                "LEFT JOIN mpa AS m ON f.mpa_id = m.mpa_id " +
                "WHERE EXTRACT(YEAR FROM f.RELEASE_DATE) = ? " +
                "GROUP BY fl.film_id " +
                "ORDER BY COUNT(fl.user_id) DESC " +
                "LIMIT ?";
        return jdbcTemplate.query(sql, new FilmMapper(), year, count);
    }

    @Override
    public List<Film> findMostLikedFilmsByGenre(Integer count, Long genreId) {
        final var sql = "SELECT f.*, m.* " +
                "FROM film_like AS fl " +
                "RIGHT JOIN film AS f ON fl.film_id = f.film_id " +
                "LEFT JOIN mpa AS m ON f.mpa_id = m.mpa_id " +
                "JOIN film_genre AS fg ON f.film_id = fg.film_id " +
                "WHERE fg.genre_id = ? " +
                "GROUP BY fl.film_id " +
                "ORDER BY COUNT(fl.user_id) DESC " +
                "LIMIT ?";
        return jdbcTemplate.query(sql, new FilmMapper(), genreId, count);
    }

    @Override
    public List<Film> findMostLikedFilmsByYearAndGenre(Integer count, Integer year, Long genreId) {
        final var sql = "SELECT f.*, m.* " +
                "FROM film_like AS fl " +
                "RIGHT JOIN film AS f ON fl.film_id = f.film_id " +
                "LEFT JOIN mpa AS m ON f.mpa_id = m.mpa_id " +
                "JOIN film_genre AS fg ON f.film_id = fg.film_id " +
                "WHERE EXTRACT(YEAR FROM f.RELEASE_DATE) = ? " +
                "      AND fg.genre_id = ? " +
                "GROUP BY f.film_id " +
                "ORDER BY COUNT(fl.user_id) DESC " +
                "LIMIT ?";
        return jdbcTemplate.query(sql, new FilmMapper(), year, genreId, count);
    }

    @Override
    public List<Long> findCommonFilmsIdByLikes(Long id) {
        final var sql = "SELECT DISTINCT fl2.film_id " +
                "FROM film_like AS fl " +
                "JOIN film_like AS fl2 " +
                "WHERE fl.user_id = ? AND fl2.user_id <> ? " +
                "EXCEPT" +
                "   (SELECT film_id " +
                "    FROM film_like " +
                "    WHERE user_id = ?)";
        return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getLong("film_id"), id, id, id);
    }

    @Override
    public void add(Long filmId, Long userId) {
        eventDao.addEvent(userId, EventType.LIKE, Operation.ADD, filmId);
        final var sql = "MERGE INTO film_like KEY (film_id, user_id) " +
                "VALUES (?, ?)";
        jdbcTemplate.update(sql, filmId, userId);
    }

    @Override
    public void delete(Long filmId, Long userId) {
        eventDao.addEvent(userId, EventType.LIKE, Operation.REMOVE, filmId);
        final var sql = "DELETE FROM film_like " +
                "WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(sql, filmId, userId);
    }
}
