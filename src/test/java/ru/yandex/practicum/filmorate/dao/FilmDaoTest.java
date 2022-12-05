package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmLikeService;


import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.yandex.practicum.filmorate.util.TestModel.getValidFilm;
import static ru.yandex.practicum.filmorate.util.TestModel.getValidUser;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDaoTest {

    private final FilmDao filmDao;

    private final UserDao userDao;

    private final MpaDao mpaDao;

    private final FilmLikeService filmLikeService;

    @Test
    @Order(1)
    public void testSave() {
        Film film = filmDao.save(getValidFilm());
        Film response = filmDao.findById(film.getId());

        assertThat(response)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L);
    }

    @Test
    @Order(2)
    public void testUpdate() {
        Film film = filmDao.findById(1L);
        film.setName("Updated");
        filmDao.update(film);

        Film response = filmDao.findById(film.getId());

        assertThat(response)
                .isNotNull()
                .hasFieldOrPropertyWithValue("name", "Updated");
    }

    @Test
    @Order(3)
    public void testFindAll() {
        Film film = getValidFilm();
        filmDao.save(film);

        List<Film> films = filmDao.findAll();

        assertThat(films)
                .isNotNull()
                .isNotEmpty()
                .hasSize(2);
    }

    @Test
    @Order(4)
    public void testFindAllWithLimit() {
        List<Film> films = filmDao.findAllWithLimit(1);

        assertThat(films)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);
    }

    @Test
    @Order(5)
    public void testFindByIds() {
        List<Film> films = filmDao.findByIds(List.of(1L, 2L));

        assertThat(films)
                .isNotNull()
                .isNotEmpty()
                .hasSize(2);

        Film film1 = films.get(0);
        Film film2 = films.get(1);

        assertThat(film1).hasFieldOrPropertyWithValue("id", 1L);
        assertThat(film2).hasFieldOrPropertyWithValue("id", 2L);
    }

    @Test
    @Order(6)
    public void testFindById() {
        Film film = filmDao.findById(1L);

        assertThat(film)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L);
    }

    @Test
    @Order(7)
    public void testDeleteById() {
        filmDao.deleteById(1L);

        List<Film> films = filmDao.findAll();

        assertThat(films)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Film film = films.get(0);

        assertThat(film.getId()).isEqualTo(2L);
    }

    @Test
    @Order(8)
    public void testGetCommonFilms() {
        Film film = getValidFilm(); //создаём 4 фильма, 2 юзера. Пересечение - фильмы 2,3
        filmDao.save(film);

        Film film2 = new Film(2L, "name2", "description2",
                LocalDate.of(2001,10,11),
                100, mpaDao.findById(2L), 2,null);
        filmDao.save(film2);
        Film film3 = new Film(3L, "name3", "description3",
                LocalDate.of(2003,9,9),
                100, mpaDao.findById(3L), 3,null);
        filmDao.save(film3);
        Film film4 = new Film(4L, "name4", "description4",
                LocalDate.of(2004,4,14),
                140, mpaDao.findById(4L), 4,null);
        filmDao.save(film4);

        User user = getValidUser();
        userDao.save(user);
        User user2 = new User(2L, "mail.ru", "login2",
                "name2", LocalDate.of(1991,5,6));
        userDao.save(user2);

        filmLikeService.like(film.getId(), user.getId());
        filmLikeService.like(film2.getId(), user.getId());
        filmLikeService.like(film3.getId(), user.getId());

        filmLikeService.like(film2.getId(), user2.getId());
        filmLikeService.like(film3.getId(), user2.getId());
        filmLikeService.like(film4.getId(), user2.getId());

        List<Film> commonFilms = filmDao.getCommonFilms(1L,2L);
        System.out.println(commonFilms);

        assertThat(commonFilms)
                .isNotNull()
                .isNotEmpty()
                .hasSize(2);
        assertTrue(commonFilms.contains(film2));
        assertTrue(commonFilms.contains(film3));

    }
}
