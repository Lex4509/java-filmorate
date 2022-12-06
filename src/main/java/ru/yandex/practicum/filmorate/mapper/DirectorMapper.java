package ru.yandex.practicum.filmorate.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Director;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DirectorMapper implements RowMapper {

    @Override
    public Director mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Director.builder()
                .id(rs.getLong("director_id"))
                .name(rs.getString("name"))
                .build();
    }
}
