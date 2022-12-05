package ru.yandex.practicum.filmorate.service.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService {
    Film like(int id, int userId);

    Film disLike(int id, int userId);

    List<Film> showMostPopularFilms(int count);
}
