package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.response.ErrorResponse;
import ru.yandex.practicum.filmorate.service.film.FilmService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/films")
@Slf4j
public class FilmController {

    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        log.info("Запрос: POST /films");
        filmService.addFilm(film);
        return film;
    }

    @GetMapping(value = "/{id}")
    public Film getFilm(@PathVariable("id") int filmId) {
        log.info("Запрос: GET /films/{}", filmId);
        return filmService.getFilm(filmId);
    }

    @GetMapping()
    public List<Film> getAllFilms() {
        log.info("Запрос: GET /films");
        return filmService.getAllFilms();
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("Запрос: PUT /films");
        filmService.updateFilm(film);
        return film;
    }

    @PutMapping(value = "/{id}/like/{userId}")
    public ResponseEntity<Film> likeFilm(@PathVariable("id") int filmId, @PathVariable int userId) {
        log.info("Запрос: PUT /films/{}/like/{}", filmId, userId);
        filmService.likeFilm(filmId, userId);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @DeleteMapping(value = "/{id}/like/{userId}")
    public ResponseEntity<Film> removeLikeFilm(@PathVariable("id") int filmId, @PathVariable int userId) {
        log.info("Запрос: DELETE /films/{}/like/{}", filmId, userId);
        filmService.removeLikeFilm(filmId, userId);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping(value = "/popular")
    public List<Film> getTopTenOrCounterFilms(@RequestParam(defaultValue = "10") int count) {
        log.info("Запрос: GET /films/popular?count={}", count);
        return filmService.getTopTenOrCounterFilms(count);

    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleInvalidIdException(final InvalidIdException e) {
        log.warn(e.getMessage());
        return new ErrorResponse("Нет такого id!", e.getMessage());
    }
}
