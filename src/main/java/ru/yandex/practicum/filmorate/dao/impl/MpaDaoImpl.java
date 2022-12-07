package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.mapper.MpaMapper;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MpaDaoImpl implements MpaDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Mpa> findAll() {
        final String sql = "SELECT * FROM mpa";
        return jdbcTemplate.query(sql, new MpaMapper());
    }

    @Override
    public Mpa findById(Long id) {
        final String sql = "SELECT * FROM mpa WHERE mpa_id = ?";

        try {
            return (Mpa) jdbcTemplate.queryForObject(sql, new MpaMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
