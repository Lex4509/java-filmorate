package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmDirectorDao;
import ru.yandex.practicum.filmorate.model.FilmDirector;
import ru.yandex.practicum.filmorate.service.FilmDirectorService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmDirectorServiceImpl implements FilmDirectorService {

    private final FilmDirectorDao filmDirectorDao;

    @Override
    public List<FilmDirector> getByFilmId(Long filmId) {
        return filmDirectorDao.findByFilmId(filmId);
    }

    @Override
    public void addDirectorsToFilm(Long filmId, Set<Long> directorsId) {

        List<FilmDirector> filmDirectors = directorsId.stream()
                .map(directorId -> FilmDirector.builder()
                        .filmId(filmId)
                        .directorId(directorId)
                        .build())
                .collect(Collectors.toList());

        if (!filmDirectors.isEmpty()) {
            filmDirectorDao.add(filmDirectors);
        }

    }

    @Override
    public void deleteDirectorsFromFilm(Long filmId) {

        filmDirectorDao.delete(filmId);

    }
}
