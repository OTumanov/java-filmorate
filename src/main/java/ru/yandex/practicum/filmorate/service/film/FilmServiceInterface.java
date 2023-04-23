package ru.yandex.practicum.filmorate.service.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

interface FilmServiceInterface {

    Optional<Film> getFilm(Integer filmId);

    List<Film> getAllFilms();

    Film addFilm(Film film);

    Film updateFilm(Film film);

    void deleteFilm(Integer filmId);

    Optional<Film> likeFilm(Integer filmId, Integer userId);

    Film removeLikeFilm(Integer filmId, Integer userId);

    List<Film> getTopFilms(Integer counter);
}

