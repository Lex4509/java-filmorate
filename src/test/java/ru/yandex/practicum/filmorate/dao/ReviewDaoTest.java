package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.yandex.practicum.filmorate.util.TestModel.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ReviewDaoTest {

    private final ReviewDao reviewDao;

    private final FilmDao filmDao;

    private final UserDao userDao;

    @Test
    @Order(1)
    void testSave() {
        User user = getValidUser();
        Film film = getValidFilm();
        userDao.save(user);
        filmDao.save(film);

        Review review = getValidReview();
        reviewDao.save(review);

        Review response = reviewDao.findById(review.getReviewId());

        assertThat(response)
                .isNotNull()
                .hasFieldOrPropertyWithValue("reviewId", 1L);

    }

    @Test
    @Order(2)
    void testUpdate() {
        Review review = reviewDao.findById(1L);
        review.setContent("Bad film");
        review.setIsPositive(true);
        reviewDao.update(review);

        Review response = reviewDao.findById(review.getReviewId());

        assertThat(response)
                .isNotNull()
                .hasFieldOrPropertyWithValue("content", "Bad film");
    }

    @Test
    @Order(3)
    void testFindByIdByCount() {
        Review review = getValidReview();
        reviewDao.save(review);

        List<Review> reviews = reviewDao.findByIdByCount(0L, 1);

        assertThat(reviews)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

    }

    @Test
    @Order(4)
    void testDeleteById() {
        reviewDao.deleteById(1L);

        List<Review> reviews = reviewDao.findByIdByCount(0L, 10);

        assertThat(reviews)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Review review = reviews.get(0);

        assertThat(review.getReviewId()).isEqualTo(2L);
    }
}