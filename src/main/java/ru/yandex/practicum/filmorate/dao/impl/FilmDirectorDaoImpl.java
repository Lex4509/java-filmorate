package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmDirectorDao;
import ru.yandex.practicum.filmorate.model.FilmDirector;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FilmDirectorDaoImpl implements FilmDirectorDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<FilmDirector> findByFilmId(Long filmId) {
        final var sql = "SELECT * " +
                "FROM film_director " +
                "WHERE film_id = ? " +
                "ORDER BY director_id";
        return jdbcTemplate.query(sql, this::mapRowToFilmDirector, filmId);
    }

    @Override
    public void add(List<FilmDirector> filmDirectors) {

        final var sql = "INSERT INTO film_director (film_id, director_id) " +
                "VALUES (?, ?)";

        jdbcTemplate.batchUpdate(
                sql,
                filmDirectors,
                100,
                (PreparedStatement ps, FilmDirector filmDirector) -> {
                    ps.setLong(1, filmDirector.getFilmId());
                    ps.setLong(2, filmDirector.getDirectorId());
                });

    }

    @Override
    public void delete(Long filmId) {

        final var sql = "DELETE FROM film_director WHERE film_id = ?";
        jdbcTemplate.update(sql, filmId);

    }

    private FilmDirector mapRowToFilmDirector(ResultSet rs, int rowNum) throws SQLException {
        return FilmDirector.builder()
                .filmId(rs.getLong("film_id"))
                .directorId(rs.getLong("director_id"))
                .build();
    }

}
