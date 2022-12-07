package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.ReviewLikeDao;

@Component
@RequiredArgsConstructor
public class ReviewLikeDaoImpl implements ReviewLikeDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addLike(Long reviewId, Long userId) {
        addRecord(reviewId, userId, true);
    }

    @Override
    public void addDisLike(Long reviewId, Long userId) {
        addRecord(reviewId, userId, false);
    }

    private void addRecord(Long reviewId, Long userId, Boolean isLike) {
        String sqlQuery = "merge into Reviews_likes (review_id, user_id, is_like) values (?, ?, ?)";
        jdbcTemplate.update(sqlQuery, reviewId, userId, isLike);
        updateRateReview(reviewId);
    }

    @Override
    public void deleteLike(Long reviewId, Long userId) {
        deleteRecord(reviewId, userId, true);
    }

    @Override
    public void deleteDislike(Long reviewId, Long userId) {
        deleteRecord(reviewId, userId, false);
    }

    private void deleteRecord(Long reviewId, Long userId, Boolean isLike) {
        String sqlQuery = "delete from Reviews_likes where review_Id = ? and user_Id = ? " +
                "and is_like = ?";
        jdbcTemplate.update(sqlQuery, reviewId, userId, isLike);
        updateRateReview(reviewId);
    }
    
    private void updateRateReview(long reviewId) {
        String sqlQuery = "update REVIEWS as r set USEFUL = " +
                "(select nvl(sum(case " +
                "           when IS_LIKE is true " +
                "           then 1 " +
                "           else -1 " +
                "           end),0) " +
                "from REVIEWS_LIKES as rl " +
                "where rl.REVIEW_ID = r.REVIEW_ID) " +
                "where r.REVIEW_ID = ?";
        jdbcTemplate.update(sqlQuery, reviewId);
    }

}
