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
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
        return jdbcTemplate.query(sql, this::mapRowToFilm);
    }

    @Override
    public List<Film> findAllWithLimit(Integer limit) {
        final var sql = "SELECT * " +
                "FROM film " +
                "LEFT JOIN mpa ON film.mpa_id = mpa.mpa_id " +
                "LIMIT ?";
        return jdbcTemplate.query(sql, this::mapRowToFilm, limit);
    }

    @Override
    public List<Film> findByIds(List<Long> filmsId) {
        final SqlParameterSource parameters = new MapSqlParameterSource("ids", filmsId);
        final var namedJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        final var sql = "SELECT * " +
                "FROM film " +
                "LEFT JOIN mpa ON film.mpa_id = mpa.mpa_id " +
                "WHERE film_id IN (:ids)";

        return namedJdbcTemplate.query(sql, parameters, this::mapRowToFilm);
    }

    @Override
    public Film findById(Long id) {
        final var sql = "SELECT * " +
                "FROM film " +
                "LEFT JOIN mpa ON film.mpa_id = mpa.mpa_id " +
                "WHERE film_id = ?";

        try {
            return jdbcTemplate.queryForObject(sql, this::mapRowToFilm, id);
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

    private Film mapRowToFilm(ResultSet rs, int rowNum) throws SQLException {
        final var mpa = Mpa.builder()
                .id(rs.getLong("mpa.mpa_id"))
                .name(rs.getString("mpa.name"))
                .build();

        return Film.builder()
                .id(rs.getLong("film_id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(rs.getInt("duration"))
                .mpa(mpa)
                .rate(rs.getInt("rate"))
                .build();
    }

    @Override
    public List<Film> getCommonFilms(Long userId, Long friendId) {

      /* String sql = "SELECT * FROM films WHERE film_id IN (" +
                    "SELECT film_id from film_like WHERE user_id = ? " +
                     "INTERSECT " +
                     "SELECT film_id from film_like WHERE user_id = ?) ";*/

        String sql = "SELECT film_id from film_like WHERE user_id = ?"  +
                      "INTERSECT " +
                      "SELECT film_id from film_like WHERE user_id = ? ";

        List<Film> films = new ArrayList<>();
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sql, userId, friendId);
        while (filmRows.next()) {
            films.add(findById(filmRows.getLong("film_id")));
        }
        return films;//.stream().sorted(Comparator.comparing(Film::getRate)).collect(Collectors.toList());
    }

        /*SqlRowSet rowSets = jdbcTemplate.queryForRowSet(sql, userId, friendId);
        while (rowSets.next()) {
            ids.add(rowSets.getLong("film_id"));
        }*/

       /*return findByIds(ids).stream()
               .sorted(Comparator.comparing(Film::getRate))
               .collect(Collectors.toList());*/


        //в H2 работает INERSECT?. Тест постман Get Common Films не проходит отдельно

       /* String sql = "SELECT film_id from film_like WHERE user_id = ? ";
        SqlRowSet rowSets1 = jdbcTemplate.queryForRowSet(sql, userId);
        List<Long> ids1 = new ArrayList<>();

        while (rowSets1.next()) {
            ids1.add(rowSets1.getLong("film_id"));
        }
        List<Film> list1 = findByIds(ids1);

        List<Long> ids2 = new ArrayList<>();
        SqlRowSet rowSets2 = jdbcTemplate.queryForRowSet(sql, friendId);
        while (rowSets2.next()) {
            ids2.add(rowSets2.getLong("film_id"));
        }
        List<Film> list2 = findByIds(ids2);


        List<Film> intersection = new ArrayList<>();
        for (Film film: list1) {
            if (list2.contains(film)) {
                intersection.add(film);
            }
        }
        return intersection.stream()
                .sorted(Comparator.comparing(Film::getRate))
                .collect(Collectors.toList());*/

}
