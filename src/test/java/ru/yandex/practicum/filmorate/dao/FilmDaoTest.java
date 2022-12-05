package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.yandex.practicum.filmorate.util.TestModel.getValidFilm;
import static ru.yandex.practicum.filmorate.util.TestModel.getValidUser;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDaoTest {

    private final FilmDao filmDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UserDao userDao;

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

        Film film = getValidFilm();
        filmDao.save(film);

        Film film2 = getValidFilm();
        film2.setId(2L);
        film2.setName("Nothing");
        film2.setRate(2);
        filmDao.save(film2);

        Film film3 = getValidFilm();
        film3.setId(3L);
        film3.setName("QWERTY");
        film2.setRate(3);
        filmDao.save(film3);

        Film film4 = getValidFilm();
        film4.setId(4L);
        film4.setName("ASDFG");
        film2.setRate(4);
        filmDao.save(film4);

        User user = getValidUser();
        userDao.save(user);

        User user2 = getValidUser();
        user2.setId(2L);
        userDao.save(user2);

        String sql1 = "Insert into film_like (film_id, user_id) values (1, 1)";
        String sql2 = "Insert into film_like (film_id, user_id) values (2, 1)";
        String sql3 = "Insert into film_like (film_id, user_id) values (3, 1)";

        jdbcTemplate.update(sql1);
        jdbcTemplate.update(sql2);
        jdbcTemplate.update(sql3);

        String sql4 = "Insert into film_like (film_id, user_id) values (2, 2)";
        String sql5 = "Insert into film_like (film_id, user_id) values (3, 2)";
        String sql6 = "Insert into film_like (film_id, user_id) values (4, 2)";

        jdbcTemplate.update(sql4);
        jdbcTemplate.update(sql5);
        jdbcTemplate.update(sql6);

        List<Film> commonFilms = filmDao.getCommonFilms(1L,2L);

        assertThat(commonFilms)
                .isNotNull()
                .isNotEmpty()
                .hasSize(2);

       // Assertions.assertEquals(commonFilms.size(), 2);

    }
}
