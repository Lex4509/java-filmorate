package ru.yandex.practicum.filmorate.util;

import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;

public final class TestModel {

    public static Film getValidFilm() {
        return Film.builder()
                .id(1L)
                .name("Test Name")
                .description("Test Description")
                .releaseDate(LocalDate.of(1895, 12, 28))
                .duration(100)
                .mpa(new Mpa(1L, ""))
                .rate(4)
                .genres(new ArrayList<>())
                .build();
    }

    public static User getValidUser() {
       return User.builder().id(1L)
                .email("test@mail.com")
                .login("TestLogin")
                .name("Test Name")
                .birthday(LocalDate.now().minusYears(25))
                .build();
    }

    public static Review getValidReview() {
        return new Review(1L, "good film", true, 1L, 1L, 0);
    }

    public static Director getValidDirector(){
        return Director.builder().id(1L)
                .name("TestDirector").build();
    }
}
