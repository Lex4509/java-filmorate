package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.EventDao;
import ru.yandex.practicum.filmorate.model.event.Event;
import ru.yandex.practicum.filmorate.service.FeedService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedServiceImpl implements FeedService {

    private final EventDao eventDao;

    @Override
    public List<Event> getUserFeed(Long id) {
        return eventDao.getUserFeed(id);
    }
}
