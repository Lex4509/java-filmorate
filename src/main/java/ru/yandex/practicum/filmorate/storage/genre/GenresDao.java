package ru.yandex.practicum.filmorate.storage.genre;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotExistException;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;

@Component
public class GenresDao {

    private final JdbcTemplate jdbcTemplate;
    private final static Logger log = LoggerFactory.getLogger(FilmStorage.class);

    public GenresDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Genre> findAll(){
        String sql = "SELECT * FROM genre";
        return jdbcTemplate.query(sql, new GenreMapper());
    }

    public Genre findById(int id){
        String sql = "SELECT * FROM genre WHERE id = ?";

        if (jdbcTemplate.query(sql, new GenreMapper(), id).size() == 0) {
            log.error("Genre not found");
            throw new NotExistException("Genre not found");
        }
        return (Genre) jdbcTemplate.query(sql, new GenreMapper(), id).get(0);
    }

}
