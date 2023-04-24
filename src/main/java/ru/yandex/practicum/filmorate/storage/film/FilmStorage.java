package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film getFilm(Integer filmId);

    List<Film> getAllFilms();

    Film saveFilm(Film film);

    Film updateFilm(Film film);

    Film deleteFilm(Integer filmId);

    Film addLike(Integer filmId, Integer userId);

    Film removeLike(Integer filmId, Integer userId);

    List<Film> getBestFilms(int count);
}
