package ru.yandex.practicum.filmorate.dao;

public interface ReviewLikeDao {

    void addLike(Long reviewId, Long userId);
    void addDisLike(Long reviewId, Long userId);

    void deleteLike(Long reviewId, Long userId);
    void deleteDislike(Long reviewId, Long userId);

}
