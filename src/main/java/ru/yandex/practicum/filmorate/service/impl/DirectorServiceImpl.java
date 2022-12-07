package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.DirectorDao;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DirectorService;

import java.util.List;

import static ru.yandex.practicum.filmorate.util.ExceptionThrowHandler.throwExceptionIfDirectorNotExists;

@Service
@RequiredArgsConstructor
public class DirectorServiceImpl implements DirectorService {

    private final DirectorDao directorDao;

    @Override
    public List<Director> getByIds(List<Long> directorId) {
        return directorDao.findByIds(directorId);
    }

    @Override
    public List<Director> getAll() {
        return directorDao.findAll();
    }

    @Override
    public Director getById(Long id) {
        final Director director = directorDao.findById(id);
        throwExceptionIfDirectorNotExists(director);

        return director;
    }

    @Override
    public Director create(Director director) {
        return directorDao.create(director);
    }

    @Override
    public Director update(Director director) {
        final var directorForCheck = directorDao.findById(director.getId());
        throwExceptionIfDirectorNotExists(directorForCheck);

        return directorDao.update(director);
    }

    @Override
    public void delete(long directorId) {

        directorDao.delete(directorId);

    }
}
