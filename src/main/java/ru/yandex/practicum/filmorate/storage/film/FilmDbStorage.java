package ru.yandex.practicum.filmorate.storage.film;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import java.sql.PreparedStatement;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class FilmDbStorage implements FilmStorage{

    private final static Logger log = LoggerFactory.getLogger(FilmStorage.class);
    private final FilmValidator filmValidator;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(FilmValidator filmValidator, JdbcTemplate jdbcTemplate) {
        this.filmValidator = filmValidator;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Film> findAll() {
        log.info("Find all films request");
        String sql = "SELECT * FROM film";
        return jdbcTemplate.query(sql, new FilmMapper(jdbcTemplate));
    }

    @Override
    public Film create(Film film) {
        log.info("Create film request");

        try {
            filmValidator.validate(film);
            String sql = "INSERT INTO film (name, description, release_date, duration, mpa_rating_id) VALUES (?,?,?,?,?)";
            String sqlGenre = "INSERT INTO film_genre (film_id, genre_id) VALUES (?,?)";

            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                PreparedStatement prepareStatement = connection.prepareStatement(sql, new String[]{"id"});
                prepareStatement.setString(1, film.getName());
                prepareStatement.setString(2, film.getDescription());
                prepareStatement.setString(3, film.getReleaseDate().format(DateTimeFormatter.ISO_DATE));
                prepareStatement.setLong(4, film.getDuration());
                prepareStatement.setInt(5, film.getMpa().getId());

                return prepareStatement;
            }, keyHolder);

            film.setId(keyHolder.getKey().intValue());

            Genre[] genres = film.getGenres();
            if (genres != null && genres.length>0) {
                for (Genre genre : genres) {
                    jdbcTemplate.update(sqlGenre, film.getId(), genre.getId());
                }
            }

            return film;
        } catch (ValidationException e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public Film update(Film film) {
        log.info("Update user request");

        try {
            filmValidator.validate(film);
            String sql = "UPDATE film SET name = ?, description = ?, release_date = ?, duration = ?, " +
                    "mpa_rating_id = ? WHERE id = ?";
            int result = jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(),
                    film.getDuration(), film.getMpa().getId(), film.getId());

            if (result == 0) {
                log.error("Film not found");
                throw new NotExistException("Film not found");
            }

            String sqlGenreDelete = "DELETE FROM film_genre WHERE film_id = ?";
            String sqlGenreUpdate = "INSERT INTO film_genre (film_id, genre_id) VALUES (?,?)";

            jdbcTemplate.update(sqlGenreDelete, film.getId());

            Genre[] genresArray = film.getGenres();

            if (genresArray != null && genresArray.length>0) {
                Set<Genre> genres = new HashSet<>(List.of(genresArray));
                for (Genre genre : genres) {
                    jdbcTemplate.update(sqlGenreUpdate, film.getId(), genre.getId());

                }
            }

            log.info("Film id " + film.getId() + " updated");

            return findById(film.getId());
        } catch (ValidationException e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public Film findById(int id) {
        log.info("Find film by id " + id + " request");

        String sql = "SELECT * FROM film WHERE id = ?";

        List<Object> result = jdbcTemplate.query(sql, new FilmMapper(jdbcTemplate), id);

        if (result.size() == 0) {
            log.error("Film not exist in db");
            throw new NotExistException("Film not exist in db");
        }

        return (Film) result.get(0);
    }
}
