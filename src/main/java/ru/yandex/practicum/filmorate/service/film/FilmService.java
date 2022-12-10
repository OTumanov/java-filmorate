package ru.yandex.practicum.filmorate.service.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.InvalidIdException;
import ru.yandex.practicum.filmorate.exception.InvalidParameterCounter;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService implements FilmServiceInterface {

    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(InMemoryFilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    @Override
    public Film getFilm(Integer filmId) {
        return returnFilmOrElseThrow(filmId);
    }

    @Override
    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    @Override
    public Film addFilm(Film film) {
        filmStorage.saveFilm(film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        returnFilmOrElseThrow(film.getId());
        filmStorage.saveFilm(film);
        return film;
    }

    @Override
    public void deleteFilm(Integer filmId) {
        returnFilmOrElseThrow(filmId);
        filmStorage.deleteFilm(filmId);
    }

    @Override
    public void likeFilm(Integer filmId, Integer userId) {
        returnFilmOrElseThrow(filmId).likeFilm(userId);
    }

    @Override
    public void removeLikeFilm(Integer filmId, Integer userId) {
        if (!returnFilmOrElseThrow(filmId).removeLikeFilm(userId)) {
            throw new InvalidIdException("Пользователь c id " + userId + " не ставил лайк фильму с id " + filmId);
        }
    }

    @Override
    public List<Film> getTopTenOrCounterFilms(Integer counter) {

        if (counter < 0) {
            throw new InvalidParameterCounter("Невеное значение переменной counter!");
        } else {

            Comparator<Film> compare = Comparator.comparing(o -> o.getLikes().size());

            return filmStorage.getAllFilms()
                    .stream()
                    .sorted(compare.reversed())
                    .limit(counter)
                    .collect(Collectors.toList());
        }
    }

    private Film returnFilmOrElseThrow(Integer filmId) {
        return filmStorage.getFilm(filmId).orElseThrow(
                () -> new FilmNotFoundException(filmId));
    }
}
