package ru.yandex.practicum.filmorate.service.impl;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.ReviewDao;
import ru.yandex.practicum.filmorate.dao.ReviewLikeDao;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewDao reviewDao;
    private final ReviewLikeDao reviewLikeDao;

    public ReviewServiceImpl(ReviewDao reviewDao, ReviewLikeDao reviewLikeDao) {
        this.reviewDao = reviewDao;
        this.reviewLikeDao = reviewLikeDao;
    }

    @Override
    public Review getById(Long id) {
        final Review review = reviewDao.findById(id);

        return review;
    }

    @Override
    public List<Review> getByIdByCount(Long filmId, Integer count) {
        return reviewDao.findByIdByCount(filmId, count);
    }

    @Override
    public Review save(Review review) {
        return reviewDao.save(review);
    }

    @Override
    public Review update(Review review) {
        return reviewDao.update(review);
    }

    @Override
    public void addLike(Long reviewId, Long userId) {
        reviewLikeDao.addLike(reviewId, userId);
    }

    @Override
    public void addDislike(Long reviewId, Long userId) {
        reviewLikeDao.addDisLike(reviewId, userId);
    }

    @Override
    public void deleteLike(Long reviewId, Long userId) {
        reviewLikeDao.deleteLike(reviewId, userId);

    }

    @Override
    public void deleteDislike(Long reviewId, Long userId) {
        reviewLikeDao.deleteDislike(reviewId, userId);
    }

    @Override
    public void deleteById(Long id) {
        reviewDao.deleteById(id);
    }
}
