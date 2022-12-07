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
import static org.junit.jupiter.api.Assertions.*;
import static ru.yandex.practicum.filmorate.util.TestModel.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ReviewLikeDaoTest {

    private final ReviewLikeDao reviewLikeDao;
    private final ReviewDao reviewDao;

    private final UserDao userDao;
    private final FilmDao filmDao;

    @Test
    @Order(1)
    void testAddLike() {
        User user = getValidUser();
        Film film = getValidFilm();
        Review review = getValidReview();
        userDao.save(user);
        filmDao.save(film);
        reviewDao.save(review);

        reviewLikeDao.addLike(1L, 1L);

        Review response = reviewDao.findById(1L);

        assertThat(response)
                .isNotNull()
                .hasFieldOrPropertyWithValue("useful", 1);
    }

    @Test
    @Order(2)
    void deleteLike() {
        reviewLikeDao.deleteLike(1L, 1L);

        Review response = reviewDao.findById(1L);

        assertThat(response)
                .isNotNull()
                .hasFieldOrPropertyWithValue("useful", 0);
    }

    @Test
    @Order(3)
    void addDisLike() {
        reviewLikeDao.addDisLike(1L, 1L);

        Review response = reviewDao.findById(1L);

        assertThat(response)
                .isNotNull()
                .hasFieldOrPropertyWithValue("useful", -1);
    }



    @Test
    @Order(4)
    void deleteDislike() {
        reviewLikeDao.deleteDislike(1L, 1L);

        Review response = reviewDao.findById(1L);

        assertThat(response)
                .isNotNull()
                .hasFieldOrPropertyWithValue("useful", 0);
    }
}