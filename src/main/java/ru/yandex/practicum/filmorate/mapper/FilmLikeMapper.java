package ru.yandex.practicum.filmorate.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.FilmLike;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FilmLikeMapper implements RowMapper<FilmLike> {

    @Override
    public FilmLike mapRow(ResultSet rs, int rowNum) throws SQLException {
        return FilmLike.builder()
                .filmId(rs.getLong("film_id"))
                .userId(rs.getLong("user_id"))
                .build();
    }
}
