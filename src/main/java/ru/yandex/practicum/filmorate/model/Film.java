package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {

    private int id;
    @NotBlank(message = "Invalid name")
    private final String name;
    private final String description;
    private final LocalDate releaseDate;
    private final long duration;
    private Set<Integer> likeUsers = new HashSet<>();

}
