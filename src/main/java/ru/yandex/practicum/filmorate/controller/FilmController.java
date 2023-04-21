package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.InvalidIdException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.response.ErrorResponse;
import ru.yandex.practicum.filmorate.storage.film.dao.FilmDbStorage;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/films")
@Slf4j
@RequiredArgsConstructor
public class FilmController {

    private final FilmDbStorage filmDbStorage;

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        log.info("Запрос: POST /films");
        filmDbStorage.saveFilm(film);
        return film;
    }

    @GetMapping(value = "/{id}")
    public Optional<Film> getFilm(@PathVariable("id") int filmId) {
        log.info("Запрос: GET /films/{}", filmId);
        return filmDbStorage.getFilm(filmId);
    }

    @GetMapping()
    public List<Film> getAllFilms() {
        log.info("Запрос: GET /films");
        return filmDbStorage.getAllFilms();
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("Запрос: PUT /films");
        filmDbStorage.updateFilm(film);
        return film;
    }

    @PutMapping(value = "/{id}/like/{userId}")
    public ResponseEntity<Film> likeFilm(@PathVariable("id") int filmId, @PathVariable int userId) {
        log.info("Запрос: PUT /films/{}/like/{}", filmId, userId);
        filmDbStorage.addLike(filmId, userId);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @DeleteMapping(value = "/{id}/like/{userId}")
    public ResponseEntity<Film> removeLikeFilm(@PathVariable("id") int filmId, @PathVariable int userId) {
        log.info("Запрос: DELETE /films/{}/like/{}", filmId, userId);
        filmDbStorage.removeLike(filmId, userId);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping(value = "/popular")
    public List<Film> getTopTenOrCounterFilms(@RequestParam(defaultValue = "10") int count) {
        log.info("Запрос: GET /films/popular?count={}", count);
        return filmDbStorage.getBestFilms(count);

    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleInvalidIdException(final InvalidIdException e) {
        log.warn(e.getMessage());
        return new ErrorResponse("Нет такого id!", e.getMessage());
    }
}
