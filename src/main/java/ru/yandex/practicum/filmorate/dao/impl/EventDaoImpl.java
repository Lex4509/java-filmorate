package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
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
        final var sql = "SELECT * FROM events WHERE user_id = ?";
        return jdbcTemplate.query(sql, this::mapRowToEvent, id);
    }

    @Override
    public void addEvent(Long userId, EventType eventType, Operation operation, Long entityId) {
        String sqlQuery = "INSERT INTO events(user_id, event_time, event_type, operation, entity_id) " +
                "VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sqlQuery, userId, System.currentTimeMillis(),eventType.toString(),
                operation.toString(), entityId);
    }

    private Event mapRowToEvent(ResultSet rs, int rowNum) throws SQLException {
        return Event.builder()
                .eventId(rs.getLong("event_id"))
                .userId(rs.getLong("user_id"))
                .timestamp(rs.getLong("event_time"))
                .eventType(EventType.valueOf(rs.getString("event_type")))
                .operation(Operation.valueOf(rs.getString("operation")))
                .entityId(rs.getLong("entity_id"))
                .build();
    }
}
