package ru.yandex.practicum.filmorate.service.impl;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {
    @Override
    public List<Review> getAll() {
        return null;
    }

    @Override
    public Review getById(Long id) {
        return null;
    }

    @Override
    public Review save(Review review) {
        return null;
    }

    @Override
    public Review update(Review review) {
        return null;
    }

    @Override
    public void addLike(Long reviewId, Long userId) {

    }

    @Override
    public void addDislike(Long reviewId, Long userId) {

    }

    @Override
    public void deleteLike(Long reviewId, Long userId) {

    }

    @Override
    public void deleteDislike(Long reviewId, Long userId) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
