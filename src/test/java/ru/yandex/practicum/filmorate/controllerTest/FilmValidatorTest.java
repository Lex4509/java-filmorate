package ru.yandex.practicum.filmorate.controllerTest;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class FilmValidatorTest {

    private final FilmValidator filmValidator = new FilmValidator();

    @Test
    void validateFilmOk() throws ValidationException {
        filmValidator.validate(new Film("Name", "Description", LocalDate.parse("2000-01-01"), 90));
    }

    @Test
    void validateFilmFailDuration() {
        Exception exception = assertThrows(ValidationException.class,
                () -> filmValidator.validate(new Film("Name", "Description", LocalDate.parse("2000-01-01"), -90)));
        assertEquals("Invalid duration", exception.getMessage());
    }

    @Test
    void validateFilmFailReleaseDate() {
        Exception exception = assertThrows(ValidationException.class,
                () -> filmValidator.validate(new Film("Name", "Description", LocalDate.parse("1800-01-01"), 90)));
        assertEquals("Invalid release date", exception.getMessage());
    }

    @Test
    void validateFilmFailDescription() {
        Exception exception = assertThrows(ValidationException.class,
                () -> filmValidator.validate(new Film("Name", new String(new char[201]).replace("\0", "7"), LocalDate.parse("2000-01-01"), 90)));
        assertEquals("Invalid description length", exception.getMessage());
    }

}
