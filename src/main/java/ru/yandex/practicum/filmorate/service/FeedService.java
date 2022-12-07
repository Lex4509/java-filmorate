package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.event.Event;

import java.util.List;

public interface FeedService {

    List<Event> getUserFeed(Long id);
}
