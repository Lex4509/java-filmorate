package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class UserDaoImpl implements UserDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<User> findAll() {
        final var sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, new UserMapper());
    }

    @Override
    public List<User> findByIds(Set<Long> usersId) {
        final SqlParameterSource parameters = new MapSqlParameterSource("ids", usersId);
        final var namedJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        final var sql = "SELECT * FROM users WHERE user_id IN (:ids)";

        return namedJdbcTemplate.query(sql, parameters, new UserMapper());
    }

    @Override
    public User findById(Long id) {
        final var sql = "SELECT * FROM users WHERE user_id = ?";

        try {
            return (User) jdbcTemplate.queryForObject(sql, new UserMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public User save(User user) {
        final SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("user_id");

        final var userId = simpleJdbcInsert.executeAndReturnKey(user.toMap());
        user.setId(userId.longValue());

        return user;
    }

    @Override
    public User update(User user) {
        final var sql = "UPDATE users " +
                "SET email = ?," +
                "    login = ?," +
                "    name = ?," +
                "    birthday = ?" +
                "WHERE user_id = ?";

        jdbcTemplate.update(
                sql,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());

        return user;
    }

    @Override
    public void deleteById(Long id) {
        final String sql = "DELETE FROM users WHERE user_id = ?";
        jdbcTemplate.update(sql, id);
    }
}
