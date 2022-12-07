package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

public interface ReviewDao {

    Review save(Review review);

    Review update(Review review);

    Review findById(Long id);

    List<Review> findByIdByCount(Long filmId, Integer count);

    void deleteById(Long id);



}
