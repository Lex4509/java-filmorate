package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.EventDao;
import ru.yandex.practicum.filmorate.model.event.Event;
import ru.yandex.practicum.filmorate.model.event.EventType;
import ru.yandex.practicum.filmorate.model.event.Operation;

import java.sql.*;
import java.util.List;

@Component
@RequiredArgsConstructor
public class EventDaoImpl implements EventDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Event> getUserFeed(Long id) {
        final var sql = "SELECT * FROM feed AS f " +
                "RIGHT JOIN event AS e ON f.event_id = e.event_id " +
                "WHERE user_id = ?";
        return jdbcTemplate.query(sql, this::mapRowToEvent, id);
    }

    @Override
    public void addEvent(Long userId, EventType eventType, Operation operation, Long entityId) {
        String sqlQuery = "INSERT INTO event(event_time, event_type, operation, entity_id) " +
                "VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"event_id"});
            stmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            stmt.setString(2, eventType.toString());
            stmt.setString(3, operation.toString());
            stmt.setLong(4, entityId);
            return stmt;
        }, keyHolder);

        addIntoFeed(userId, getLastId());

    }

    private void addIntoFeed(Long userId, Long eventId) {
        String sqlQuery = "INSERT INTO feed(user_id, event_id) " +
                "VALUES (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery,
                    new String[]{"user_id", "event_id"});
            stmt.setLong(1, userId);
            stmt.setLong(2, eventId);
            return stmt;
        }, keyHolder);
    }

    private Long getLastId() {
        SqlRowSet idRows = jdbcTemplate.queryForRowSet("SELECT * FROM event " +
                "GROUP BY event_id ORDER BY event_id DESC LIMIT 1;");
        idRows.next();
        return idRows.getLong(1);
    }

    private Event mapRowToEvent(ResultSet rs, int rowNum) throws SQLException {
        return Event.builder()
                .eventId(rs.getLong("event_id"))
                .timestamp(rs.getTimestamp("event_time"))
                .userId(rs.getLong("user_id"))
                .eventType(EventType.valueOf(rs.getString("event_type")))
                .operation(Operation.valueOf(rs.getString("operation")))
                .entityId(rs.getLong("entity_id"))
                .build();
    }
}
