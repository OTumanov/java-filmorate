package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.InvalidParameterCounter;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.film.dao.FilmDbStorage;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService implements FilmServiceInterface {

    private final FilmDbStorage filmDbStorage;
    private final UserService userService;

    @Override
    public Optional<Film> getFilm(Integer filmId) {
        log.info("Сервис: Запрос фильма с id {}", filmId);

        checkFilm(filmId);
        return filmDbStorage.getFilm(filmId);
    }

    @Override
    public List<Film> getAllFilms() {
        log.info("Сервис: Запрос всех фильмов");

        return filmDbStorage.getAllFilms();
    }

    @Override
    public Film addFilm(Film film) {
        log.info("Сервис: Запрос на добавление фильма с названием {}, описанием {}, датой релиза {}, продолжительностью {}, MPA {} и жанром {}",
                film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getMpa(), film.getGenres());
        filmDbStorage.saveFilm(film);

        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        log.info("Сервис: Запрос на обновление фильма {} с id {}", film.getName(), film.getId());
        checkFilm(film.getId());
        returnFilmOrThrow(film.getId());

        return filmDbStorage.updateFilm(film);
    }

    @Override
    public void deleteFilm(Integer filmId) {
        log.info("Сервис: Запрос на удаление фильма с id {}", filmId);
        returnFilmOrThrow(filmId);
        filmDbStorage.deleteFilm(filmId);
    }

    @Override
    public Optional<Film> likeFilm(Integer filmId, Integer userId) {
        log.info("Сервис: Запрос на лайк фильму с id {} от пользователя с id {}", filmId, userId);
        returnFilmOrThrow(filmId);
        userService.returnUserOrThrow(userId);

        return filmDbStorage.addLike(filmId, userId);
    }

    @Override
    public Film removeLikeFilm(Integer filmId, Integer userId) {
        log.info("Сервис: Запрос на удаление лайка фильму с id {} от пользователя с id {}", filmId, userId);
        checkFilm(filmId);
        userService.checkUser(userId);

        return filmDbStorage.removeLike(filmId, userId);

    }

    @Override
    public List<Film> getTopFilms(Integer counter) {
        log.info("Сервис: Запрос на получение топ{} фильмов", counter);

        if (counter < 0) {
            throw new InvalidParameterCounter("Счетчик должен быть положительным!");
        } else {
            return filmDbStorage.getBestFilms(counter);
        }
    }

    public void checkFilm(Integer filmId) {
        filmDbStorage.checkFilm(filmId);
    }

    private Film returnFilmOrThrow(Integer filmId) {

        return filmDbStorage.getFilm(filmId).orElseThrow(() -> {
                    log.info("Сервис: Запрос на проверку фильма с id {}", filmId);
                    return new ObjectNotFoundException("Фильм с id " + filmId + " не найден");
                }
        );
    }
}
