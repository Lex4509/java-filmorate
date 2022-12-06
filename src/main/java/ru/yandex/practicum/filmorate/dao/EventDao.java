package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.event.Event;
import ru.yandex.practicum.filmorate.model.event.EventType;
import ru.yandex.practicum.filmorate.model.event.Operation;

import java.util.List;

public interface EventDao {
    List<Event> getUserFeed(Long id);

    void addEvent(Long userId, EventType eventType, Operation operation, Long entityId);
}
