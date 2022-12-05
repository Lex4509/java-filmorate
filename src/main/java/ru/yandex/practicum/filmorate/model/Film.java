package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.sql.Array;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {

    private int id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private long duration;
    private Set<Integer> likeUsers = new HashSet<>();
    private MPA mpa;
    private Genre[] genres;

    public Film(int id, String name, String description, LocalDate releaseDate, long duration, MPA mpa, Genre[] genres) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
        this.genres = genres;

    }
}
