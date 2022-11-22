package ru.yandex.practicum.filmorate.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FilmMapper implements RowMapper {

    private final JdbcTemplate jdbcTemplate;

    public FilmMapper(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {


        int id = rs.getInt("id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        LocalDate releaseDate = rs.getDate("release_date").toLocalDate();
        long duration = rs.getLong("duration");
        int mpaId = rs.getInt("mpa_rating_id");

        String sql = "SELECT * FROM mpa_rating WHERE id = ?";

        MPA mpa = (MPA) jdbcTemplate.query(sql, new MpaMapper(), mpaId).get(0);

        String sqlGenre = "SELECT * FROM genre WHERE id in (SELECT genre_id FROM film_genre WHERE film_id = ?)";

        List<Genre> genresList = jdbcTemplate.query(sqlGenre, new GenreMapper(), id);
        Genre[] genres = new ArrayList<>(genresList).toArray(new Genre[0]);

        return new Film(id, name, description, releaseDate, duration, mpa, genres);
    }
}
