package ru.yandex.practicum.filmorate.storage.mpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotExistException;
import ru.yandex.practicum.filmorate.mapper.MpaMapper;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;

@Component
public class MpaDao {

    private final JdbcTemplate jdbcTemplate;
    private final static Logger log = LoggerFactory.getLogger(FilmStorage.class);

    @Autowired
    public MpaDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<MPA> findAll(){
        String sql = "SELECT * FROM mpa_rating";
        return jdbcTemplate.query(sql, new MpaMapper());
    }

    public MPA findById(int id){
        String sql = "SELECT * FROM mpa_rating WHERE id = ?";

        if (jdbcTemplate.query(sql, new MpaMapper(), id).size() == 0) {
            log.error("MPA not found");
            throw new NotExistException("MPA not found");
        }
        return (MPA) jdbcTemplate.query(sql, new MpaMapper(), id).get(0);
    }

}
