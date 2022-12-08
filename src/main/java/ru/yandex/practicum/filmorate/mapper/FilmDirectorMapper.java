package ru.yandex.practicum.filmorate.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.FilmDirector;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FilmDirectorMapper implements RowMapper<FilmDirector> {

    @Override
    public FilmDirector mapRow(ResultSet rs, int rowNum) throws SQLException {
        return FilmDirector.builder()
                .filmId(rs.getLong("film_id"))
                .directorId(rs.getLong("director_id"))
                .build();
    }
}
