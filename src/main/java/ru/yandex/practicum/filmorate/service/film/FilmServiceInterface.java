package ru.yandex.practicum.filmorate.service.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

 interface FilmServiceInterface {

     Film getFilm(Integer filmId);

     List<Film> getAllFilms();

     Film addFilm(Film film);

     Film updateFilm(Film film);

     void deleteFilm(Integer filmId);

     void likeFilm(Integer filmId, Integer userId);

     void removeLikeFilm(Integer filmId, Integer userId);

     List<Film> getTopTenOrCounterFilms(Integer counter);
}

