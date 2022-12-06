package ru.yandex.practicum.filmorate.mapper;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.FilmGenre;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FilmGenreMapper implements RowMapper {

    @Override
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        return FilmGenre.builder()
                .filmId(rs.getLong("film_id"))
                .genreId(rs.getLong("genre_id"))
                .build();
    }
}
