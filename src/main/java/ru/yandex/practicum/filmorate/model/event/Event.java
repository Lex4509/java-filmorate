package ru.yandex.practicum.filmorate.model.event;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
public class Event {

    private Long eventId;
    private Long userId;
    private Timestamp timestamp;
    private EventType eventType;
    private Operation operation;
    private Long entityId;
}
