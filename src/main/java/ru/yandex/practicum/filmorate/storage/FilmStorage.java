package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {
        Optional<Film> getFilm(Integer filmId);
        List<Film> getAllFilms();
        Film saveFilm(Film film);
        void deleteFilm(Integer filmId);
        void deleteAllFilms();
        Optional<List<Integer>> getLikeOfFilm(Integer filmId);

}
