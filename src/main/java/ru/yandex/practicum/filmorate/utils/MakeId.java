package ru.yandex.practicum.filmorate.utils;

public class MakeId {

    private int id;

    public MakeId() {
        this.id = 0;
    }

    public int gen() {
        return ++id;
    }
}