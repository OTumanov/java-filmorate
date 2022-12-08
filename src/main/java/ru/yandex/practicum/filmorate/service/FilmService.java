package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.InvalidIdException;
import ru.yandex.practicum.filmorate.exception.InvalidParameterCounter;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(InMemoryFilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film getFilm(Integer filmId) {
        return returnFilmOrElseThrow(filmId);
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film addFilm(Film film) {
        filmStorage.saveFilm(film);
        return film;
    }

    public Film updateFilm(Film film) {
        returnFilmOrElseThrow(film.getId());
        filmStorage.saveFilm(film);
        return film;
    }

    public void deleteFilm(Integer filmId) {
        returnFilmOrElseThrow(filmId);
        filmStorage.deleteFilm(filmId);
    }

    public void likeFilm(Integer filmId, Integer userId) {
        returnFilmOrElseThrow(filmId).likeFilm(userId);
    }

    public void removeLikeFilm(Integer filmId, Integer userId) {
        if (!returnFilmOrElseThrow(filmId).removeLikeFilm(userId)) {
            throw new InvalidIdException("Пользователь c id " + userId + " не ставил лайк фильму с id " + filmId);
        }
    }

    public List<Film> getTopTenOrCounterFilms(Integer counter) {

        if (counter < 0) {
            throw new InvalidParameterCounter("Невеное значение переменной counter!");
        } else {

            Comparator<Film> compare = Comparator.comparing(o -> o.getLikes().size());

            List<Film> collect = filmStorage.getAllFilms()
                    .stream()
                    .sorted(compare.reversed())
                    .limit(counter)
                    .collect(Collectors.toList());
            return collect;
        }
    }
    private Film returnFilmOrElseThrow(Integer filmId) {
        return filmStorage.getFilm(filmId).orElseThrow(
                () -> new FilmNotFoundException(filmId));
    }
}
