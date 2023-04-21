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

    Optional<Film> addLike(int filmId, int userId);

    Film removeLike(int filmId, int userId);

    List<Film> getBestFilms(int count);
}
