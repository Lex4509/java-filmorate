package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.ReviewService;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService, UserService userService, FilmService filmService) {
        this.reviewService = reviewService;
    }

    //добавление нового
    @PostMapping
    public Review create(@Valid @RequestBody Review review) {
        log.info("Сохраняется {}", review.toString());

        reviewService.save(review);

        return review;
    }

    //обновление
    @PutMapping
    public Review update(@Valid @RequestBody Review review) {
        log.info("Обновляется {}", review.toString());

        reviewService.update(review);

        return review;
    }

    //получение по id
    @GetMapping("/{id}")
    public Review get(@PathVariable long id) {
        log.info("GET Отзыв id={}", id);

        return reviewService.getById(id);
    }

    //Получение всех отзывов по идентификатору фильма, если фильм не указа но все.
    //Если кол-во не указано то 10.
    @GetMapping
    public List<Review> getFilmIdByCount(@RequestParam(defaultValue = "0") Long filmId, @RequestParam(defaultValue = "10") Integer count) {
        log.info("GET Отзыв id={}, count={}", filmId, count);

        return reviewService.getByIdByCount(filmId, count);
    }

    //пользователь ставит лайк отзыву
    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable long id, @PathVariable long userId) {
        log.info("Добавляетсвя лайк к отзыву id={} пользователем userId={}", id, userId);

        reviewService.addLike(id, userId);
    }

    //пользователь ставит дизлайк отзыву
    @PutMapping("/{id}/dislike/{userId}")
    public void addDislike(@PathVariable long id, @PathVariable long userId) {
        log.info("Добавляетсвя дизлайк к отзыву id={} пользователем userId={}", id, userId);

        reviewService.addDislike(id, userId);
    }

    //пользователь удаляет лайк отзыву
    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable long id, @PathVariable long userId) {
        log.info("Удаляется лайк к отзыву id={} пользователем userId={}", id, userId);

        reviewService.deleteLike(id, userId);
    }

    //пользователь удаляет дизлайк отзыву
    @DeleteMapping("/{id}/dislike/{userId}")
    public void deleteDisLike(@PathVariable long id, @PathVariable long userId) {
        log.info("Удаляется дизлайк к отзыву id={} пользователем userId={}", id, userId);

        reviewService.deleteDislike(id, userId);
    }

    //удаление отзыва
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable long id) {
        log.info("Удаляется отзыв id={} ", id);

        reviewService.deleteById(id);
    }


}
