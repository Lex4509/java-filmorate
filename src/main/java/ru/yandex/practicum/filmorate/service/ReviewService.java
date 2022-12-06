package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

public interface ReviewService {

    //получение
    Review getById(Long id);

    List<Review> getByIdByCount(Long filmId, Integer count);

    //добавление/изменение
    Review save(Review review);

    Review update(Review review);

    //оценки
    void addLike(Long reviewId, Long userId);

    void addDislike(Long reviewId, Long userId);

    void deleteLike(Long reviewId, Long userId);

    void deleteDislike(Long reviewId, Long userId);

    //удаление
    void deleteById(Long id);

}
