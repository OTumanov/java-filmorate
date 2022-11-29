package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.utils.MakeId;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private static final LocalDate DATE = LocalDate.of(1895, 12, 28);
    private final MakeId id = new MakeId();
    private final Map<Integer, Film> films = new HashMap<>();

    @GetMapping
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<Film> addFilm(@Valid @RequestBody Film film) {

        if (checkAllPOST(film)) {
            return new ResponseEntity<>(film, HttpStatus.INTERNAL_SERVER_ERROR);

        } else {
            log.info("Запрос POST /films " + film);
            film.setId(id.gen());
            films.put(film.getId(), film);

            return new ResponseEntity<>(film, HttpStatus.CREATED);
        }
    }

    @PutMapping
    @ResponseBody
    public ResponseEntity<Film> updateFilm(@Valid @RequestBody Film film) {

        if (checkAllPUT(film)) {
            return new ResponseEntity<>(film, HttpStatus.INTERNAL_SERVER_ERROR);

        } else {
            log.info("Запрос PUT /films " + film);
            films.put(film.getId(), film);

            return new ResponseEntity<>(film, HttpStatus.OK);
        }
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<String> handleException(ValidationException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean checkAllPOST(Film film) {
        return film.getReleaseDate().isBefore(DATE) || film.getDuration() <= 0 || films.containsKey(film.getId());
    }

    private boolean checkAllPUT(Film film) {
        return film.getReleaseDate().isBefore(DATE) || film.getDuration() <= 0 || !films.containsKey(film.getId());
    }
}

