package ru.yandex.practicum.filmorate.service.mpa;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface MpaServiceInterface {
    List<Mpa> getAllMpa();

    Mpa getMpa(int id);
}
