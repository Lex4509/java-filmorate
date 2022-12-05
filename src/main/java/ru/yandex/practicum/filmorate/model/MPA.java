package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class MPA {

    private int id;
    private String name;

    public MPA(int id, String mpa) {

        this.id = id;
        this.name = mpa;

    }
}
