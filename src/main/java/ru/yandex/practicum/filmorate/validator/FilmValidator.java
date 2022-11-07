package ru.yandex.practicum.filmorate.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

@Component
public class FilmValidator {

    private final static Logger log = LoggerFactory.getLogger(FilmValidator.class);

    private final int DESCRIPTION_MAX_LENGTH = 200;
    private final LocalDate MIN_RELEASE_DATE= LocalDate.parse("1895-12-28");

    public void validate(Film film) throws ValidationException {
        if (film.getDescription().length()>DESCRIPTION_MAX_LENGTH) {
            log.error("Too large film description");
            throw new ValidationException("Invalid description length");
        }
        if (film.getDuration()<=0) {
            log.error("Duration should be positive");
            throw new ValidationException("Invalid duration");
        }
        if (film.getReleaseDate().isBefore(MIN_RELEASE_DATE)) {
            log.error("Release date should be after {}", MIN_RELEASE_DATE);
            throw new ValidationException("Invalid release date");
        }
    }

}
