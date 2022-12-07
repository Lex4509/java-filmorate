package ru.yandex.practicum.filmorate.service.impl;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.ReviewDao;
import ru.yandex.practicum.filmorate.dao.ReviewLikeDao;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.ReviewService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewDao reviewDao;
    private final ReviewLikeDao reviewLikeDao;
    private final UserService userService;
    private final FilmService filmService;

    public ReviewServiceImpl(ReviewDao reviewDao, ReviewLikeDao reviewLikeDao, UserService userService,
                             FilmService filmService) {
        this.reviewDao = reviewDao;
        this.reviewLikeDao = reviewLikeDao;
        this.userService = userService;
        this.filmService = filmService;
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
        validate(review);
        return reviewDao.save(review);
    }

    @Override
    public Review update(Review review) {
        validate(review);
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

    private void validate(Review review) {
        //проверка пользователя
        userService.getById(review.getUserId());
        //проверка фильм
        filmService.getById(review.getFilmId());
    }
}
