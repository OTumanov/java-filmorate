package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping(value = "/films")
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;

    @GetMapping(value = "/{id}")
    public Optional<Film> getFilm(@PathVariable("id") int filmId) {
        log.info("Контроллер: запрос фильма с id {}", filmId);

        return filmService.getFilm(filmId);
    }

    @GetMapping()
    public List<Film> getAllFilms() {
        log.info("Контроллер: запрос всех фильмов");

        return filmService.getAllFilms();
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        log.info("Контроллер: запрос на добавление фильма {} с id {}", film.getName(), film.getId());

        return filmService.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("Контроллер: запрос на обновление фильма {} с id {}", film.getName(), film.getId());

        return filmService.updateFilm(film);
    }

    @PutMapping(value = "/{id}/like/{userId}")
    public Optional<Film> likeFilm(@PathVariable("id") int filmId, @PathVariable int userId) {
        log.info("Контроллер: запрос на добавление лайка фильму с id {} от пользователя с id {}", filmId, userId);

        return filmService.likeFilm(filmId, userId);
    }

    @DeleteMapping(value = "/{id}/like/{userId}")
    public void removeLikeFilm(@PathVariable("id") int filmId, @PathVariable int userId) {
        log.info("Контроллер: запрос на удаление лайка фильму с id {} от пользователя с id {}", filmId, userId);
        filmService.removeLikeFilm(filmId, userId);
    }

    @GetMapping(value = "/popular")
    public List<Film> getTopFilms(@RequestParam(defaultValue = "10") int count) {
        log.info("Контроллер: запрос на получение лучших {} фильмов", count);

        return filmService.getTopFilms(count);
    }
}
