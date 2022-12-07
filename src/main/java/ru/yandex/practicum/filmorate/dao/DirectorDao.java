package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;

public interface DirectorDao {

    List<Director> findByIds(List<Long> directorId);

    List<Director> findAll();

    Director findById(Long id);

    Director create(Director director);

    Director update(Director director);

    void delete(long id);

}
