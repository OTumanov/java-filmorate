package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.utils.MakerId;

import java.util.*;

@Repository
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Integer, Film> films = new HashMap<>();

    private final MakerId id = new MakerId();

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
