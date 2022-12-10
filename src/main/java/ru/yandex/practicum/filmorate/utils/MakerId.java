package ru.yandex.practicum.filmorate.utils;

public class MakerId {

    private int id;

    public MakerId() {
        this.id = 0;
    }

    public int gen() {
        return ++id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
