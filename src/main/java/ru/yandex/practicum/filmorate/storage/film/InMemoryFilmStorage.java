package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.utils.MakeId;

import java.util.*;
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Integer, Film> films = new HashMap<>();

    private final MakeId id = new MakeId();

    @Override
    public Film saveFilm(Film film) {
        if (film.getId() == null) film.setId(id.gen());
        films.put(film.getId(), film);
        return film;
    }
    @Override
    public Optional<Film> getFilm(Integer filmId) {
        return Optional.ofNullable(films.get(filmId));
    }

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public void deleteFilm(Integer filmId) {
        films.remove(filmId);
    }

    @Override
    public void deleteAllFilms() {
        films.clear();
    }

    @Override
    public Optional<List<Integer>> getLikeOfFilm(Integer filmId) {
        if (getFilm(filmId).isPresent()) {
            return Optional.of(new ArrayList<>(films.get(filmId).getLikes()));
        } else {
            return Optional.empty();
        }
    }
}
