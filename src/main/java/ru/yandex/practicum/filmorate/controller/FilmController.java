package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    @GetMapping
    public List<Film> getAll() {
        return filmService.getAll();
    }

    @GetMapping("/{id}")
    public Film getById(@PathVariable @NotNull Long id) {
        return filmService.getById(id);
    }

    @GetMapping("/popular")
    public List<Film> getMostLikedFilms(@RequestParam(name = "count", defaultValue = "10", required = false)
                                        Integer count,
                                        @RequestParam(name = "year", required = false) Integer year,
                                        @RequestParam(name = "genreId", required = false) Long genreId) {
        if (count <= 0) count = 10;
        return filmService.getMostLikedFilms(count, year, genreId);
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        return filmService.save(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        return filmService.update(film);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public void like(@PathVariable Long filmId, @PathVariable Long userId) {
        filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public void dislike(@PathVariable Long filmId, @PathVariable Long userId) {
        filmService.deleteLike(filmId, userId);
    }

    @GetMapping("/common")
    public List<Film> getCommonFilms(@RequestParam(name = "userId") Long userId,
                                     @RequestParam(name = "friendId") Long friendId) {
        return filmService.getCommonFilms(userId, friendId);
    }
    @DeleteMapping("/{filmId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteFilmByID(@PathVariable Long filmId) {
        filmService.deleteById(filmId);
    }

    @GetMapping("/director/{directorId}")
    public List<Film> getDirectorFilmsSorted(@PathVariable long directorId,
                                             @RequestParam(name = "sortBy") String sortBy){
        return filmService.getDirectorFilmsSorted(directorId, sortBy);
    }

    @GetMapping("/search")
    public List<Film> searchFilms(@RequestParam String query, @RequestParam String by){
        return filmService.searchFilms(query, by);
    }
}
