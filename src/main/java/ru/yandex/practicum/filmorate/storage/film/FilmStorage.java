package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    Optional<Film> getFilm(Integer filmId);

    List<Film> getAllFilms();

    Film saveFilm(Film film);

    Film updateFilm(Film film);

    Optional<Film> deleteFilm(Integer filmId);

    Optional<Film> addLike(Integer filmId, Integer userId);

    Film removeLike(Integer filmId, Integer userId);

    List<Film> getBestFilms(int count);
}
