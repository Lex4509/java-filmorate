package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.EventDao;
import ru.yandex.practicum.filmorate.dao.ReviewDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.event.EventType;
import ru.yandex.practicum.filmorate.model.event.Operation;

import java.sql.PreparedStatement;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ReviewDaoImpl implements ReviewDao {

    private final JdbcTemplate jdbcTemplate;
    private final BeanPropertyRowMapper<Review> reviewMapper = new BeanPropertyRowMapper<>(Review.class);
    private final EventDao eventDao;


    @Override
    public Review save(Review review) {
        String sqlQuery = "insert into reviews (content, is_positive, user_id, film_id, useful) " + "VALUES (?,?,?,?,?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(sqlQuery, new String[]{"review_id"});
            statement.setString(1, review.getContent());
            statement.setBoolean(2, review.getIsPositive());
            statement.setLong(3, review.getUserId());
            statement.setLong(4, review.getFilmId());
            statement.setLong(5, review.getUseful());
            return statement;
        }, keyHolder);
        review.setReviewId(keyHolder.getKey().longValue());

        eventDao.addEvent(review.getUserId(), EventType.REVIEW, Operation.ADD, keyHolder.getKey().longValue());

        return review;

    }

    @Override
    public Review update(Review review) {
        final var sql = "UPDATE reviews SET " + "content = ?, " + "is_positive = ? " + "WHERE review_id = ?";

        jdbcTemplate.update(sql, review.getContent(), review.getIsPositive(), review.getReviewId());

        try {
            eventDao.addEvent(findById(review.getReviewId()).getUserId(), EventType.REVIEW, Operation.UPDATE,
                    review.getReviewId());
        } catch (NotFoundException ignored) {}

        return review;
    }

    @Override
    public Review findById(Long id) {
        String sqlQuery = "select * from reviews where reviews.review_id = ?";

        final List<Review> reviews = jdbcTemplate.query(sqlQuery, reviewMapper, id);
        if (reviews.size() != 1) {
            throw new NotFoundException("Отзыв id=" + id);
        }
        return reviews.get(0);
    }

    @Override
    public List<Review> findByIdByCount(Long filmId, Integer count) {
        if (filmId != 0) {
            String sqlQuery = "select * from reviews where reviews.film_id = ? " + "order by useful desc limit ?";
            return jdbcTemplate.query(sqlQuery, reviewMapper, filmId, count);
        } else {
            String sqlQuery = "select * from reviews " + "order by useful desc limit ?";
            return jdbcTemplate.query(sqlQuery, reviewMapper, count);
        }
    }

    @Override
    public void deleteById(Long id) {

        try {
            eventDao.addEvent(findById(id).getUserId(), EventType.REVIEW, Operation.REMOVE, findById(id).getReviewId());
        } catch (NotFoundException ignored) {}
        String sqlQuery = "delete from reviews where REVIEW_ID = ?";
        jdbcTemplate.update(sqlQuery, id);
    }
}
