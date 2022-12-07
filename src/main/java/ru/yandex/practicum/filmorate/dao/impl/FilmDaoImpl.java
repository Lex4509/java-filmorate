package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FilmDaoImpl implements FilmDao {


    private final JdbcTemplate jdbcTemplate;

    @Autowired
    private final MpaDao mpaDao;

    @Override
    public List<Film> findAll() {
        final var sql = "SELECT * " +
                "FROM film " +
                "LEFT JOIN mpa ON film.mpa_id = mpa.mpa_id";
        return jdbcTemplate.query(sql, new FilmMapper());
    }

    @Override
    public List<Film> findByIds(List<Long> filmsId) {
        final SqlParameterSource parameters = new MapSqlParameterSource("ids", filmsId);
        final var namedJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        final var sql = "SELECT * " +
                "FROM film " +
                "LEFT JOIN mpa ON film.mpa_id = mpa.mpa_id " +
                "WHERE film_id IN (:ids)";

        return namedJdbcTemplate.query(sql, parameters, new FilmMapper());
    }

    @Override
    public Film findById(Long id) {
        final var sql = "SELECT * " +
                "FROM film " +
                "LEFT JOIN mpa ON film.mpa_id = mpa.mpa_id " +
                "WHERE film_id = ?";

        try {
            return jdbcTemplate.queryForObject(sql, new FilmMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Film save(Film film) {
        final SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("film")
                .usingGeneratedKeyColumns("film_id");

        final var filmId = simpleJdbcInsert.executeAndReturnKey(film.toMap());
        film.setId(filmId.longValue());

        return film;
    }

    @Override
    public Film update(Film film) {
        final var sql = "UPDATE film " +
                "SET name = ?, " +
                "description = ?, " +
                "release_date = ?, " +
                "duration = ?, " +
                "mpa_id = ?, " +
                "rate = ? " +
                "WHERE film_id = ?";

        jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getRate(),
                film.getId());

        return film;
    }

    @Override
    public void deleteById(Long id) {
        final var sql = "DELETE FROM film WHERE film_id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public List<Film> getDirectorFilmsSortedByYear(long directorId) {
        final var sql = "SELECT * " +
                "FROM film as f " +
                "LEFT JOIN mpa ON f.mpa_id = mpa.mpa_id " +
                "JOIN film_director as fd on f.film_id = fd.film_id " +
                "WHERE fd.director_id = ? " +
                "ORDER BY f.release_date";
        return jdbcTemplate.query(sql, new FilmMapper(), directorId);
    }

    @Override
    public List<Film> getDirectorFilmsSortedByLike(long directorId) {
        final var sql = "SELECT * " +
                "FROM film as f " +
                "LEFT JOIN mpa ON f.mpa_id = mpa.mpa_id " +
                "JOIN film_director as fd on f.film_id = fd.film_id " +
                "LEFT JOIN film_like as fl on f.film_id = fl.film_id " +
                "GROUP BY f.film_id " +
                "HAVING fd.director_id = ? " +
                "ORDER BY COUNT(f.film_id) DESC";
        return jdbcTemplate.query(sql, new FilmMapper(), directorId);
    }

    @Override
    public List<Film> getCommonFilms(Long userId, Long friendId) {

        String sql = "SELECT film_id from film_like WHERE user_id = ?" +
                "INTERSECT " +
                "SELECT film_id from film_like WHERE user_id = ? ";
        List<Long> listId = new ArrayList<>();
        SqlRowSet idRow = jdbcTemplate.queryForRowSet(sql, userId, friendId);
        while (idRow.next()) {
            listId.add(idRow.getLong("film_id"));
        }

        return findByIds(listId);
    }

}
