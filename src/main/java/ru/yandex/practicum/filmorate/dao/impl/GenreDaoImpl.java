package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GenreDaoImpl implements GenreDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Genre> findByIds(List<Long> genresId) {
        final SqlParameterSource parameters = new MapSqlParameterSource("ids", genresId);
        final var namedJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        final var sql = "SELECT * FROM genre WHERE genre_id IN (:ids)";

        return namedJdbcTemplate.query(sql, parameters, new GenreMapper());
    }

    @Override
    public List<Genre> findAll() {
        final String sql = "SELECT * FROM genre";

        return jdbcTemplate.query(sql, new GenreMapper());
    }

    @Override
    public Genre findById(Long id) {
        final String sql = "SELECT * FROM genre WHERE genre_id = ?";

        try {
            return (Genre) jdbcTemplate.queryForObject(sql, new GenreMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
