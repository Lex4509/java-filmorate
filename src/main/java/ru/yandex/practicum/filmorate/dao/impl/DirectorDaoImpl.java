package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.DirectorDao;
import ru.yandex.practicum.filmorate.model.Director;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DirectorDaoImpl implements DirectorDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Director> findByIds(List<Long> directorId) {
        final SqlParameterSource parameters = new MapSqlParameterSource("ids", directorId);
        final var namedJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        final var sql = "SELECT * FROM director WHERE director_id IN (:ids)";

        return namedJdbcTemplate.query(sql, parameters, this::mapRowToDirector);
    }

    @Override
    public List<Director> findAll() {
        final String sql = "SELECT * FROM director";

        return jdbcTemplate.query(sql, this::mapRowToDirector);
    }

    @Override
    public Director findById(Long id) {
        final String sql = "SELECT * FROM director WHERE director_id = ?";

        try {
            return jdbcTemplate.queryForObject(sql, this::mapRowToDirector, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Director create(Director director) {
        final SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("director")
                .usingGeneratedKeyColumns("director_id");

        final var directorId = simpleJdbcInsert.executeAndReturnKey(director.toMap());
        director.setId(directorId.longValue());

        return director;
    }

    @Override
    public Director update(Director director) {
        final var sql = "UPDATE director " +
                "SET name = ? " +
                "WHERE director_id = ?";

        jdbcTemplate.update(sql,
                director.getName(),
                director.getId());

        return director;
    }

    @Override
    public void delete(long id) {
        final var sql = "delete FROM director WHERE director_id = ?";

        jdbcTemplate.update(sql, id);
    }

    private Director mapRowToDirector(ResultSet rs, int rowNum) throws SQLException {
        return Director.builder()
                .id(rs.getLong("director_id"))
                .name(rs.getString("name"))
                .build();
    }

}
