package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.yandex.practicum.filmorate.util.TestModel.getValidDirector;
import static ru.yandex.practicum.filmorate.util.TestModel.getValidFilm;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DirectorDaoTest {

    private final DirectorDao directorDao;

    @Test
    @Order(1)
    public void testCreate() {
        Director director = directorDao.create(getValidDirector());
        Director response = directorDao.findById(director.getId());

        assertThat(response)
                .isNotNull()
                .hasFieldOrPropertyWithValue("name", "TestDirector");
    }

    @Test
    @Order(2)
    public void testFindAll() {
        Director director = directorDao.create(getValidDirector());
        directorDao.create(director);

        List<Director> directors = directorDao.findAll();

        assertThat(directors)
                .isNotNull()
                .isNotEmpty()
                .hasSize(2);
    }

    @Test
    @Order(3)
    public void testFindById() {
        Director testDirector = directorDao.create(getValidDirector());
        directorDao.create(testDirector);

        Director director = directorDao.findById(1L);

        assertThat(director)
                .isNotNull()
                .hasFieldOrPropertyWithValue("name", "TestDirector");
    }

}
