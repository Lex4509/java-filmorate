package ru.yandex.practicum.filmorate.service.film;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.util.List;

@Component
public class FilmDbService implements FilmService{

    private final FilmDbStorage filmDbStorage;
    private final UserDbStorage userDbStorage;
    private final JdbcTemplate jdbcTemplate;
    private final static Logger log = LoggerFactory.getLogger(FilmService.class);

    @Autowired
    public FilmDbService(FilmDbStorage filmDbStorage, UserDbStorage userDbStorage, JdbcTemplate jdbcTemplate) {
        this.filmDbStorage = filmDbStorage;
        this.userDbStorage = userDbStorage;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film like(int id, int userId) {
        log.info("Like film " + id + " by user " + userId + " request");

        Film film = filmDbStorage.findById(id);
        userDbStorage.findById(userId);

        String sql = "INSERT INTO likes (user_id, film_id) VALUES (?,?)";

        jdbcTemplate.update(sql, userId, id);

        return film;
    }

    @Override
    public Film disLike(int id, int userId) {
        log.info("Dislike film " + id + " by user " + userId + " request");

        Film film = filmDbStorage.findById(id);
        filmDbStorage.findById(userId);

        String sql = "DELETE FROM likes WHERE user_id = ? AND film_id = ?";

        jdbcTemplate.update(sql, userId, id);

        return film;
    }

    @Override
    public List<Film> showMostPopularFilms(int count) {
        log.info("Most popular " + count + " films request");

        String sql = "SELECT id, name, description, release_date, duration, mpa_rating_id FROM film f " +
                "LEFT JOIN likes l ON f.id = l.film_id\n" +
                "GROUP BY f.id\n" +
                "ORDER BY COUNT(l.film_id) DESC\n" +
                "LIMIT ?";

        return jdbcTemplate.query(sql, new FilmMapper(jdbcTemplate), count);
    }
}
