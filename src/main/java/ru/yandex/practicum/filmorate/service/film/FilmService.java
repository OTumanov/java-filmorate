package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.InvalidIdException;
import ru.yandex.practicum.filmorate.exception.InvalidParameterCounter;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.film.dao.FilmDbStorage;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService implements FilmServiceInterface {

    private final FilmDbStorage filmDbStorage;
    private final UserService userService;

    @Override
    public Optional<Film> getFilm(Integer filmId) {
        return filmDbStorage.getFilm(filmId);
    }

    @Override
    public List<Film> getAllFilms() {
        return filmDbStorage.getAllFilms();
    }

    @Override
    public Film addFilm(Film film) {
        filmDbStorage.saveFilm(film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        returnFilmOrElseThrow(film.getId());
        filmDbStorage.saveFilm(film);

        return film;
    }

    @Override
    public void deleteFilm(Integer filmId) {
        returnFilmOrElseThrow(filmId);
        filmDbStorage.deleteFilm(filmId);
    }

    @Override
    public Optional<Film> likeFilm(Integer filmId, Integer userId) {
        returnFilmOrElseThrow(filmId);
        userService.returnUserOrElseThrow(userId);

        return filmDbStorage.addLike(filmId, userId);
    }

    @Override
    public void removeLikeFilm(Integer filmId, Integer userId) {
        userService.returnUserOrElseThrow(userId);
        returnFilmOrElseThrow(filmId);

        if (!returnFilmOrElseThrow(filmId).removeLikeFilm(userId)) {
            throw new InvalidIdException("Пользователь c id " + userId + " не ставил лайк фильму с id " + filmId);
        } else {
            filmDbStorage.removeLike(filmId, userId);
        }
    }

    @Override
    public List<Film> getTopTenOrCounterFilms(Integer counter) {

        if (counter < 0) {
            throw new InvalidParameterCounter("Неверное значение переменной counter!");
        } else {

            Comparator<Film> compare = Comparator.comparing(o -> o.getLikes().size());

            return filmDbStorage.getAllFilms()
                    .stream()
                    .sorted(compare.reversed())
                    .limit(counter)
                    .collect(Collectors.toList());
        }
    }

    private Film returnFilmOrElseThrow(Integer filmId) {

        return filmDbStorage.getFilm(filmId).orElseThrow(
                () -> new FilmNotFoundException(filmId)
        );
    }
}
