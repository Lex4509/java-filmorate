package ru.yandex.practicum.filmorate.aplicationTest.film;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDaoTest {

    private final FilmDbStorage filmDbStorage;

    @BeforeEach
    public void init(){

        MPA mpa = new MPA(1,"test");
        Genre[] genres = new Genre[1];
        genres[0] = new Genre(1, "test");

        Film film = new Film(1, "test", "test description", LocalDate.parse("2000-01-01"), 90, mpa, genres);
        filmDbStorage.create(film);
    }

    @Test
    public void testFindById() {

        Optional<Film> userOptional = Optional.ofNullable(filmDbStorage.findById(1));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    public void testFindFilms() {

        Optional<Film> userOptional = Optional.ofNullable(filmDbStorage.findAll().get(0));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    public void updateFilm() {

        MPA mpa = new MPA(1,"test");
        Genre[] genres = new Genre[1];
        genres[0] = new Genre(1, "test");

        Optional<Film> userOptional = Optional.ofNullable(filmDbStorage.update(
                new Film(1, "name upd", "test description", LocalDate.parse("2000-01-01"),
                        90, mpa, genres)));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("name", "name upd")
                );
    }

}
