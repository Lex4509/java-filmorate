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
    private Timestamp timestamp;
    private EventType eventType;
    private Operation operation;
    private Long entityId;
    private Long userId;

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("event_time", timestamp);
        values.put("event_type", eventType);
        values.put("operation", operation);
        values.put("entity_id", entityId);

        return values;
    }
}
